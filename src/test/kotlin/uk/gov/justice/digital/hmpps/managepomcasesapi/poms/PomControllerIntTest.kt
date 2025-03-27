package uk.gov.justice.digital.hmpps.managepomcasesapi.poms

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.PrisonApiExtension.Companion.prisonApi

class PomControllerIntTest : IntegrationTestBase() {
  companion object {
    private const val POM_URI = "/poms/{staffId}/is-pom/{prisonCode}"
  }

  @Nested
  @DisplayName("GET $POM_URI")
  inner class IsPomEndpoint {
    @BeforeEach
    fun stubPrisonApi() {
      hmppsAuth.stubGrantToken()
      prisonApi.stubHasPomRoleResponse()
    }

    @Test
    fun `should return unauthorized if no token`() {
      webTestClient.get()
        .uri(POM_URI, 12345, "LEI")
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should return forbidden if no role`() {
      webTestClient.get()
        .uri(POM_URI, 12345, "LEI")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return forbidden if wrong role`() {
      webTestClient.get()
        .uri(POM_URI, 12345, "LEI")
        .headers(setAuthorisation(roles = listOf("ROLE_WRONG")))
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return true if staff member is a POM in the prison`() {
      prisonApi.stubHasPomRoleResponse(200, "true")

      webTestClient.get()
        .uri(POM_URI, 12345, "LEI")
        .headers(setAuthorisation(roles = listOf("ROLE_MANAGE_POM_CASES__MANAGE_POM_CASES_UI")))
        .exchange()
        .expectStatus()
        .isOk
        .expectBody(Boolean::class.java)
        .isEqualTo(true)
    }

    @Test
    fun `should return false if staff member is not a POM in the prison`() {
      prisonApi.stubHasPomRoleResponse(200, "false")

      webTestClient.get()
        .uri(POM_URI, 12345, "LEI")
        .headers(setAuthorisation(roles = listOf("ROLE_MANAGE_POM_CASES__MANAGE_POM_CASES_UI")))
        .exchange()
        .expectStatus()
        .isOk
        .expectBody(Boolean::class.java)
        .isEqualTo(false)
    }

    @Test
    fun `should return false if prison API returns a 404`() {
      prisonApi.stubHasPomRoleResponse(404)

      webTestClient.get()
        .uri(POM_URI, 12345, "LEI")
        .headers(setAuthorisation(roles = listOf("ROLE_MANAGE_POM_CASES__MANAGE_POM_CASES_UI")))
        .exchange()
        .expectStatus()
        .isOk
        .expectBody(Boolean::class.java)
        .isEqualTo(false)
    }

    @Test
    fun `should return error if prison API returns an error other than 404`() {
      prisonApi.stubHasPomRoleResponse(500)

      webTestClient.get()
        .uri(POM_URI, 12345, "LEI")
        .headers(setAuthorisation(roles = listOf("ROLE_MANAGE_POM_CASES__MANAGE_POM_CASES_UI")))
        .exchange()
        .expectStatus()
        .is5xxServerError
    }
  }
}
