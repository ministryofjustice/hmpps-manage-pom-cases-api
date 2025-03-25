package uk.gov.justice.digital.hmpps.managepomcasesapi.client

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData

@Service
class PrisonerSearchClient(
  private val prisonerSearchApiWebClient: WebClient,
) {
  // Swagger docs: https://prisoner-search.prison.service.justice.gov.uk/swagger-ui/index.html#/Popular/findByPrison
  fun findByPrison(prisonCode: String): List<CaseData> {
    val results = mutableListOf<CaseData>()
    var moreResultsToRead = true
    var page = 0 // Zero-based page index
    val size = 1500 // The size of the page to be returned

    while (moreResultsToRead) {
      val response = prisonerSearchApiWebClient.get()
        .uri("/prisoner-search/prison/{prisonCode}?page={page}&size={size}&include-restricted-patients=true", prisonCode, page, size)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
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

  data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
  )
  data class PaginatedResponse(
    val totalElements: Int,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val content: List<CaseData>,
    val pageable: Pageable? = null,
  )
}
