package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocatedCasesService
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.MpcCasesService

@Service
class ParoleCasesService(
  private val mpcCasesService: MpcCasesService,
  private val allocatedCasesService: AllocatedCasesService,
  private val paroleReviewsRepository: ParoleReviewRepository,
) {
  fun upcomingAt(prisonCode: String): List<UpcomingParoleCase> {
    val cases = mpcCasesService.forPrison(prisonCode).associateBy { it.caseId }
    val allocatedCases = allocatedCasesService.forPrison(prisonCode).associateBy { it.caseId }
    val paroleReviews = paroleReviewsRepository.findByCaseIdIn(allocatedCases.keys.toList())

    print(allocatedCases)

    return paroleReviews
      .map {
        ParoleCase(
          paroleReview = it,
          caseData = cases[it.caseId]!!,
          allocatedCase = allocatedCases[it.caseId]!!,
        )
      }
      .filter { it.upcomingReview() }
      .map { it.asUpcomingParoleCase() }
  }
}
