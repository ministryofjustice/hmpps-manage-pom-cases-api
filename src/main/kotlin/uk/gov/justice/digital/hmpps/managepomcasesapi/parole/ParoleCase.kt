package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocatedCase
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData

class ParoleCase(
  private val caseData: CaseData,
  private val paroleReview: ParoleReview,
  private val allocatedCase: AllocatedCase,
) {
  fun asUpcomingParoleCase() = UpcomingParoleCase(
    caseId = caseData.caseId,
    firstName = caseData.firstName,
    lastName = caseData.lastName,
    pomFirstName = allocatedCase.pomFirstName,
    pomLastName = allocatedCase.pomLastName,
    pomId = allocatedCase.pomId,
    pomRole = "@SUPPORTING@",
    paroleDateValue = nextParoleDate?.date,
    paroleDateType = nextParoleDate?.type,
  )

  fun upcomingReview() = nextParoleDate != null

  private val nextParoleDate: ParoleDate?
    get() = paroleDates.find { it.upcoming() }

  private val paroleDates: List<ParoleDate>
    get() = listOf(
      ParoleDate("Target Hearing Date", paroleReview.targetHearingDate),
      ParoleDate("Tariff End Date", caseData.tariffDate),
      ParoleDate("Parole Eligibility Date", caseData.paroleEligibilityDate),
    )
}
