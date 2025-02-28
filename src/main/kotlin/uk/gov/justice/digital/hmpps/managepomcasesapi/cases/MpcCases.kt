package uk.gov.justice.digital.hmpps.managepomcasesapi.cases

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.service.PrisonerSearchService

@Service
class MpcCases(
  private val prisonerSearchService: PrisonerSearchService,
) {
  fun forPrison(prisonCode: String): List<CaseData> = prisonerSearchService
    .findByPrison(prisonCode)
    .filterNot { it.imprisonmentStatus == "A_FINE" }
    .filter { ACCEPTABLE_LEGAL_STATUSES.contains(it.legalStatus) }

  companion object {
    val ACCEPTABLE_LEGAL_STATUSES = listOf("SENTENCED", "INDETERMINATE_SENTENCE", "RECALL", "IMMIGRATION_DETAINEE")
  }
}
