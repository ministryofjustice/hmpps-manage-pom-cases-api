package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData
import uk.gov.justice.digital.hmpps.managepomcasesapi.client.PrisonerSearchClient
import uk.gov.justice.digital.hmpps.managepomcasesapi.client.PrisonerSearchClient.PaginatedResponse
import uk.gov.justice.digital.hmpps.managepomcasesapi.support.StubbedRequests

class PrisonerSearchClientTest {
  private val prisonerSearchApiWebClient = mock<WebClient>()
  private val stubbedRequests = StubbedRequests(prisonerSearchApiWebClient)

  @Test
  fun `Searching for prisoners at a given prison returns a list of prisoner details`() {
    val paginatedResponses = listOf(
      PaginatedResponse(
        totalElements = 4,
        totalPages = 2,
        first = true,
        last = false,
        content = listOf(
          CaseData(prisonerNumber = "GAX123"),
          CaseData(prisonerNumber = "GAX456"),
          CaseData(prisonerNumber = "GAX789"),
        ),
      ),
      PaginatedResponse(
        totalElements = 4,
        totalPages = 2,
        first = false,
        last = true,
        content = listOf(
          CaseData(prisonerNumber = "GAX101112"),
        ),
      ),
    )

    paginatedResponses.forEachIndexed { i, response ->
      stubbedRequests.get(
        "/prisoner-search/prison/{prisonCode}?page={page}&size={size}&include-restricted-patients=true",
        "LEI",
        i,
        1500,
        response = response,
      )
    }

    val prisonerSearch = PrisonerSearchClient(prisonerSearchApiWebClient)
    val results = prisonerSearch.findByPrison("LEI")
    Assertions.assertEquals(4, results.size)
    Assertions.assertEquals(listOf("GAX123", "GAX456", "GAX789", "GAX101112"), results.map { it.prisonerNumber })
  }
}
