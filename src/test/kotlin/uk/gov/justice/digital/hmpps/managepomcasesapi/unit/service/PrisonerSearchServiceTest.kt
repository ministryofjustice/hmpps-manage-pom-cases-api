package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.managepomcasesapi.service.PrisonerSearchService
import uk.gov.justice.digital.hmpps.managepomcasesapi.service.PrisonerSearchService.PaginatedResponse
import uk.gov.justice.digital.hmpps.managepomcasesapi.support.StubbedRequests

class PrisonerSearchServiceTest {
  private val prisonerSearchWebClient = mock<WebClient>()
  private val stubbedRequests = StubbedRequests(prisonerSearchWebClient)

  @Test
  fun `Searching for prisoners at a given prison returns a list of prisoner details`() {
    val paginatedResponses = listOf(
      PaginatedResponse(
        totalElements = 4,
        totalPages = 2,
        first = true,
        last = false,
        content = listOf(
          PrisonerSearchService.Prisoner(prisonerNumber = "GAX123"),
          PrisonerSearchService.Prisoner(prisonerNumber = "GAX456"),
          PrisonerSearchService.Prisoner(prisonerNumber = "GAX789"),
        ),
      ),
      PaginatedResponse(
        totalElements = 4,
        totalPages = 2,
        first = false,
        last = true,
        content = listOf(
          PrisonerSearchService.Prisoner(prisonerNumber = "GAX101112"),
        ),
      ),
    )

    paginatedResponses.forEachIndexed { i, response ->
      stubbedRequests.get(
        "/prisoner-search/prison/{prisonCode}?page={page}",
        "LEI",
        i + 1,
        response = response,
      )
    }

    val prisonerSearch = PrisonerSearchService(prisonerSearchWebClient)
    val results = prisonerSearch.findByPrison("LEI")
    Assertions.assertEquals(4, results.size)
    Assertions.assertEquals(listOf("GAX123", "GAX456", "GAX789", "GAX101112"), results.map { it.prisonerNumber })
  }
}
