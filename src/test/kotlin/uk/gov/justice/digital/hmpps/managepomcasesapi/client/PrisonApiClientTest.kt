package uk.gov.justice.digital.hmpps.managepomcasesapi.client

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.managepomcasesapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.PrisonApiExtension.Companion.prisonApi

class PrisonApiClientTest : IntegrationTestBase() {
  @Autowired
  lateinit var prisonApiClient: PrisonApiClient

  @BeforeEach
  fun setup() {
    hmppsAuth.stubGrantToken()
  }

  @Test
  fun `Retrieving details for a given staff id`() {
    prisonApi.stubStaffDetailResponse(
      response = """{"staffId":"1234", "firstName":"Jane", "lastName":"Smith", "status": "ACTIVE"}"""
        .trimIndent(),
    )

    val result = prisonApiClient.staffDetail(1234)

    Assertions.assertThat(result).isNotNull
    Assertions.assertThat(result?.staffId).isEqualTo(1234)
    Assertions.assertThat(result?.firstName).isEqualTo("Jane")
    Assertions.assertThat(result?.lastName).isEqualTo("Smith")
    Assertions.assertThat(result?.status).isEqualTo("ACTIVE")
  }

  @Test
  fun `Checking if a staff member has POM role in a prison`() {
    prisonApi.stubHasPomRoleResponse(response = "true")

    val result = prisonApiClient.hasPomRole(1234, "MDI")

    Assertions.assertThat(result).isTrue
  }
}
