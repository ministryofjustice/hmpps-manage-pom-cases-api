package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.Allocation
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocationsService
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.MpcCasesService

@Service
class ParoleCasesService(
  private val mpcCasesService: MpcCasesService,
  private val allocationsService: AllocationsService,
  private val paroleReviewsRepository: ParoleReviewRepository,
) {
  fun upcomingAt(prisonCode: String): List<ParoleCase> = upcomingParoleDatesAt(prisonCode).map { (case, data) ->
    var (paroleDate, allocation) = data
    ParoleCase(
      caseId = case.caseId,
      firstName = case.firstName,
      lastName = case.lastName,
      pomId = allocation?.pomId,
      pomFirstName = allocation?.pomFirstName,
      pomLastName = allocation?.pomLastName,
      pomRole = "@SUPPORTING@",
      paroleDateValue = paroleDate.nextUpcomingDate()?.date,
      paroleDateType = paroleDate.nextUpcomingDate()?.type,
    )
  }

  private fun upcomingParoleDatesAt(prisonCode: String): Map<CaseData, Pair<ParoleDates, Allocation>> {
    val cases = mpcCasesService.forPrison(prisonCode).associateBy { it.caseId }
    val allocations = allocationsService.forCasesAtPrison(cases.keys.toList(), prisonCode).associateBy { it.caseId }
    val paroleReviews = paroleReviewsRepository.latestReviewsFor(allocations.keys.toList()).associateBy { it.caseId }

    return allocations
      .mapKeys { (caseId, _) -> cases[caseId]!! }
      .mapValues { (case, allocation) ->
        Pair(
          ParoleDates.from(case, paroleReviews[case.caseId]),
          allocation,
        )
      }
      .filterValues { it.first.nextUpcomingDate() != null }
  }
}
