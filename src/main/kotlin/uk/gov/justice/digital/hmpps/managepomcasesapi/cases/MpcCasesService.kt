package uk.gov.justice.digital.hmpps.managepomcasesapi.cases

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.client.PrisonerSearchClient

@Service
class MpcCasesService(
  private val prisonerSearchClient: PrisonerSearchClient,
) {
  @Cacheable("MpcCasesService.forPrison")
  fun forPrison(prisonCode: String): List<CaseData> = prisonerSearchClient
    .findByPrison(prisonCode)
    .filterNot { REJECTED_IMPRISONMENT_STATUSES.contains(it.imprisonmentStatus) }
    .filter { ACCEPTABLE_LEGAL_STATUSES.contains(it.legalStatus) }

  companion object {
    val ACCEPTABLE_LEGAL_STATUSES = listOf("SENTENCED", "INDETERMINATE_SENTENCE", "RECALL", "IMMIGRATION_DETAINEE")
    val REJECTED_IMPRISONMENT_STATUSES = listOf("A_FINE")
  }
}
