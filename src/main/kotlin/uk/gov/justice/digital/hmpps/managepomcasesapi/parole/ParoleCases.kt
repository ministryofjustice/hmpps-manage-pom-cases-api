package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocatedCases
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.MpcCases

@Service
class ParoleCases(
  private val mpcCases: MpcCases,
  private val allocatedCases: AllocatedCases,
  private val paroleReviewsRepository: ParoleReviewRepository,
) {
  fun upcomingAt(prisonCode: String): List<UpcomingParoleCase> {
    val cases = mpcCases.forPrison(prisonCode).associateBy { it.caseId }
    val allocatedCases = allocatedCases.forPrison(prisonCode).associateBy { it.caseId }
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
