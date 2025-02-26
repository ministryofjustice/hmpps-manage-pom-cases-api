package uk.gov.justice.digital.hmpps.managepomcasesapi.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

@Service
class StaffService(
  private val prisonApiWebClient: WebClient,
) {

  // Swagger docs: https://prison-api.prison.service.justice.gov.uk/swagger-ui/index.html#/staff/getStaffDetail
  fun staffDetail(staffId: Int): StaffDetail? = prisonApiWebClient.get()
    .uri("/api/staff/{staffId}", staffId)
    .retrieve()
    .bodyToMono(StaffDetail::class.java)
    .onErrorResume(WebClientResponseException.NotFound::class.java) { Mono.empty() }
    .block()

  data class StaffDetail(
    val staffId: Int,
    val firstName: String,
    val lastName: String,
    val status: String,
  )
}
