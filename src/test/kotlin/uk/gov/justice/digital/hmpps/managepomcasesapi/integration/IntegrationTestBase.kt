package uk.gov.justice.digital.hmpps.managepomcasesapi.integration

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.HmppsAuthApiExtension
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.PrisonApiExtension
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.PrisonApiExtension.Companion.prisonApi
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.PrisonerSearchApiExtension
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.PrisonerSearchApiExtension.Companion.prisonerSearchApi
import uk.gov.justice.hmpps.test.kotlin.auth.JwtAuthorisationHelper

@ExtendWith(HmppsAuthApiExtension::class, PrisonApiExtension::class, PrisonerSearchApiExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  @Autowired
  protected lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthorisationHelper

  internal fun setAuthorisation(
    username: String? = "AUTH_ADM",
    roles: List<String> = listOf(),
    scopes: List<String> = listOf("read"),
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisationHeader(username = username, scope = scopes, roles = roles)

  protected fun stubPingWithResponse(status: Int) {
    hmppsAuth.stubHealthPing(status)
    prisonApi.stubHealthPing(status)
    prisonerSearchApi.stubHealthPing(status)
  }
}
