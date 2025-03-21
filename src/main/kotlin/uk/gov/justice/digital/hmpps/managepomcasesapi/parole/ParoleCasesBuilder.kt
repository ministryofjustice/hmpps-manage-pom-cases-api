package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.Allocation
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId
import uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility.CaseResponsibility

class ParoleCasesBuilder {
  private var joinedCases = hashMapOf<NomisId, JoinedCase>()

  val caseIds: List<NomisId> by lazy {
    joinedCases.keys.toList()
  }

  val allocatedCaseIds: List<NomisId> by lazy {
    joinedCases.filterValues { it.allocation != null }.keys.toList()
  }

  fun addCase(case: CaseData) {
    joinedCases[case.caseId] = JoinedCase(case)
  }

  fun addAllocation(allocation: Allocation) {
    joinedCases[allocation.caseId]?.allocation = allocation
  }

  fun addParoleReview(paroleReview: ParoleReview) {
    joinedCases[paroleReview.caseId]?.paroleReview = paroleReview
  }

  fun addResponsibility(responsibility: CaseResponsibility) {
    joinedCases[responsibility.caseId]?.responsibility = responsibility
  }

  fun upcomingParoleCases(): List<ParoleCase> = joinedCases
    .filterValues { it.allocation != null }
    .filterValues { it.nextUpcomingParoleDate() != null }
    .values
    .map {
      ParoleCase(
        caseId = it.case.caseId,
        firstName = it.case.firstName,
        lastName = it.case.lastName,
        pomId = it.allocation?.pomId,
        pomFirstName = it.allocation?.pomFirstName,
        pomLastName = it.allocation?.pomLastName,
        pomRole = it.responsibility?.pomRole(),
        paroleDateValue = it.nextUpcomingParoleDate()?.date,
        paroleDateType = it.nextUpcomingParoleDate()?.type,
      )
    }

  class JoinedCase(val case: CaseData) {
    var allocation: Allocation? = null
    var paroleReview: ParoleReview? = null
    var responsibility: CaseResponsibility? = null

    fun nextUpcomingParoleDate() = ParoleDates.from(case, paroleReview).nextUpcomingDate()
  }
}
