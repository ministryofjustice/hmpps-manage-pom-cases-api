package uk.gov.justice.digital.hmpps.managepomcasesapi.cases

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.service.PrisonerSearchService

@Service
class MpcCases(
  private val prisonerSearchService: PrisonerSearchService,
) {
  @Cacheable("MpcCases.forPrison")
  fun forPrison(prisonCode: String): List<CaseData> = prisonerSearchService
    .findByPrison(prisonCode)
    .filterNot { REJECTED_IMPRISONMENT_STATUSES.contains(it.imprisonmentStatus) }
    .filter { ACCEPTABLE_LEGAL_STATUSES.contains(it.legalStatus) }

  companion object {
    val ACCEPTABLE_LEGAL_STATUSES = listOf("SENTENCED", "INDETERMINATE_SENTENCE", "RECALL", "IMMIGRATION_DETAINEE")
    val REJECTED_IMPRISONMENT_STATUSES = listOf("A_FINE")
  }
}
