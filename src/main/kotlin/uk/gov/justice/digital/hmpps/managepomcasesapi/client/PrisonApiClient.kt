package uk.gov.justice.digital.hmpps.managepomcasesapi.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

@Service
class PrisonApiClient(@Qualifier("prisonApiWebClient") private val webClient: WebClient) {

  companion object {
    val LOG: Logger = LoggerFactory.getLogger(this::class.java)
  }

  // Swagger docs: https://prison-api.prison.service.justice.gov.uk/swagger-ui/index.html#/staff/getStaffDetail
  fun staffDetail(staffId: Int): StaffDetail? = webClient.get()
    .uri("/api/staff/{staffId}", staffId)
    .retrieve()
    .bodyToMono(StaffDetail::class.java)
    .onErrorResume(WebClientResponseException.NotFound::class.java) { Mono.empty() }
    .block()

  // Swagger docs: https://prison-api.prison.service.justice.gov.uk/swagger-ui/index.html#/staff/hasStaffRole
  fun hasPomRole(staffId: Int, agencyId: String): Boolean = webClient.get()
    .uri("/api/staff/{staffId}/{agencyId}/roles/POM", staffId, agencyId)
    .retrieve()
    .bodyToMono(Boolean::class.java)
    .onErrorResume { e ->
      LOG.error("hasPomRole: {}", e.message)
      if (e is WebClientResponseException.NotFound) {
        Mono.just(false)
      } else {
        Mono.error(e)
      }
    }
    .block()!!

  data class StaffDetail(
    val staffId: Int,
    val firstName: String,
    val lastName: String,
    val status: String,
  )
}
