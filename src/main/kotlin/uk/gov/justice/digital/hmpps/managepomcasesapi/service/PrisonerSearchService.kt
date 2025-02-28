package uk.gov.justice.digital.hmpps.managepomcasesapi.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData

@Service
class PrisonerSearchService(
  private val prisonerSearchApiWebClient: WebClient,
) {

  // Swagger docs: https://prisoner-search.prison.service.justice.gov.uk/swagger-ui/index.html#/Popular/findByPrison
  fun findByPrison(prisonCode: String): List<CaseData> {
    val results = mutableListOf<CaseData>()
    var moreResultsToRead = true
    var page = 1

    while (moreResultsToRead) {
      val response = prisonerSearchApiWebClient.get()
        .uri("/prisoner-search/prison/{prisonCode}?page={page}", prisonCode, page)
        .retrieve()
        .bodyToMono(PaginatedResponse::class.java)
        .onErrorResume(WebClientResponseException.NotFound::class.java) { Mono.empty() }
        .block()
      if (response == null) {
        break
      } else {
        results += response.content
        moreResultsToRead = !response.last
        page += 1
      }
    }

    return results
  }

  data class PaginatedResponse(
    val totalElements: Int,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val content: List<CaseData>,
  )
}
