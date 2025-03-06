package uk.gov.justice.digital.hmpps.managepomcasesapi.integration.parole

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.PrisonApiExtension.Companion.prisonApi
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.UpcomingParoleCase

class ParoleCasesControllerIntTest : IntegrationTestBase() {

  @Nested
  @DisplayName("GET /parole-cases/upcoming")
  inner class GetUpcomingParoleCasesEndpoint {
    @BeforeEach
    fun stubPrisonApi() {
      hmppsAuth.stubGrantToken()
      prisonApi.stubUncheckedPrisonerSearchResponse()
    }

    @Test
    fun `should return unauthorized if no token`() {
      webTestClient.get()
        .uri("/parole-cases/upcoming/{prisonCode}", "LEI")
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should return forbidden if no role`() {
      webTestClient.get()
        .uri("/parole-cases/upcoming/{prisonCode}", "LEI")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return forbidden if wrong role`() {
      webTestClient.get()
        .uri("/parole-cases/upcoming/{prisonCode}", "LEI")
        .headers(setAuthorisation(roles = listOf("ROLE_WRONG")))
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return 404 if no prison provided`() {
      webTestClient.get()
        .uri("/parole-cases/upcoming")
        .headers(setAuthorisation(roles = listOf("ROLE_MANAGE_POM_CASES__MANAGE_POM_CASES_UI")))
        .exchange()
        .expectStatus()
        .isNotFound
    }

    @Test
    fun `should return OK`() {
      prisonApi.stubUncheckedPrisonerSearchResponse()
      webTestClient.get()
        .uri("/parole-cases/upcoming/{prisonCode}", "LEI")
        .headers(setAuthorisation(roles = listOf("ROLE_MANAGE_POM_CASES__MANAGE_POM_CASES_UI")))
        .exchange()
        .expectStatus().isOk
        .expectBody(object : ParameterizedTypeReference<List<UpcomingParoleCase>>() {})
        .returnResult()
    }
  }

}
