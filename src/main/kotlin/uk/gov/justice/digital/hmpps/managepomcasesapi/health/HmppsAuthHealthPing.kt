package uk.gov.justice.digital.hmpps.managepomcasesapi.health

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.hmpps.kotlin.health.HealthPingCheck

@Component("hmppsAuth")
class HmppsAuthHealthPing(@Qualifier("hmppsAuthHealthWebClient") webClient: WebClient) : HealthPingCheck(webClient)

@Component("prisonApi")
class HmppsPrisonApiHealthPing(@Qualifier("prisonApiHealthWebClient") webClient: WebClient) : HealthPingCheck(webClient)

@Component("prisonerSearchApi")
class HmppsPrisonerSearchApiHealthPing(@Qualifier("prisonerSearchApiHealthWebClient") webClient: WebClient) : HealthPingCheck(webClient)
