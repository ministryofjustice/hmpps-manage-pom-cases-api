package uk.gov.justice.digital.hmpps.managepomcasesapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.hmpps.kotlin.auth.authorisedWebClient
import uk.gov.justice.hmpps.kotlin.auth.healthWebClient
import java.time.Duration

@Configuration
class WebClientConfiguration(
  @Value("\${hmpps-auth.url}") val hmppsAuthBaseUri: String,
  @Value("\${hmpps-prisoner-search-api.url}") val hmppsPrisonerSearchApiBaseUri: String,
  @Value("\${hmpps-prison-api.url}") val hmppsPrisonApiBaseUri: String,
  @Value("\${api.health-timeout:2s}") val healthTimeout: Duration,
  @Value("\${api.timeout:20s}") val timeout: Duration,
) {
  @Bean
  fun hmppsAuthHealthWebClient(builder: WebClient.Builder): WebClient = builder.healthWebClient(hmppsAuthBaseUri, healthTimeout)

  @Bean
  fun prisonApiHealthWebClient(builder: WebClient.Builder): WebClient = builder.healthWebClient(hmppsPrisonApiBaseUri, healthTimeout)

  @Bean
  fun prisonerSearchApiHealthWebClient(builder: WebClient.Builder): WebClient = builder.healthWebClient(hmppsPrisonerSearchApiBaseUri, healthTimeout)

  @Bean
  fun prisonApiWebClient(authorizedClientManager: OAuth2AuthorizedClientManager, builder: WebClient.Builder): WebClient = builder.authorisedWebClient(authorizedClientManager, registrationId = "hmpps-prison-api", url = hmppsPrisonApiBaseUri, timeout)

  @Bean
  fun prisonerSearchApiWebClient(authorizedClientManager: OAuth2AuthorizedClientManager, builder: WebClient.Builder): WebClient = builder.authorisedWebClient(authorizedClientManager, registrationId = "hmpps-prisoner-search-api", url = hmppsPrisonerSearchApiBaseUri, timeout)
}
