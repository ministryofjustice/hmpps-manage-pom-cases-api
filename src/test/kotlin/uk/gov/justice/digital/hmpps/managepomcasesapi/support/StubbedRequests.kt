package uk.gov.justice.digital.hmpps.managepomcasesapi.support

import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class StubbedRequests(private val webClient: WebClient) {
  private val requestBodyUriSpec = mock<WebClient.RequestBodyUriSpec>()

  fun <T> get(path: String, vararg params: Any, response: T & Any) {
    val requestBodySpec = mock<WebClient.RequestBodySpec>()
    val responseSpec = mock<WebClient.ResponseSpec>()

    Mockito.`when`(webClient.get()).thenReturn(requestBodyUriSpec)
    Mockito.`when`(requestBodyUriSpec.uri(path, params)).thenReturn(requestBodySpec)
    Mockito.`when`(requestBodySpec.retrieve()).thenReturn(responseSpec)
    Mockito.`when`(responseSpec.bodyToMono(response::class.java)).thenReturn(Mono.just(response))
  }
}
