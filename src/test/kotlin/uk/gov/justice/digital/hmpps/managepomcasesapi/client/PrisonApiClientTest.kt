package uk.gov.justice.digital.hmpps.managepomcasesapi.client

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.managepomcasesapi.support.StubbedRequests

class PrisonApiClientTest {
  private val prisonApiWebClient = mock<WebClient>()
  private val stubbedRequests = StubbedRequests(prisonApiWebClient)

  @Test
  fun `Retrieving details for a given staff id`() {
    stubbedRequests.get(
      "/api/staff/{staffId}",
      1234,
      response = PrisonApiClient.StaffDetail(
        staffId = 1234,
        firstName = "Jane",
        lastName = "Smith",
        status = "ACTIVE",
      ),
    )

    val client = PrisonApiClient(prisonApiWebClient)
    val result = client.staffDetail(1234)

    Assertions.assertThat(result).isNotNull
    Assertions.assertThat(result?.staffId).isEqualTo(1234)
  }

  @Test
  fun `Checking if a staff member has POM role in a prison`() {
    stubbedRequests.get(
      "/api/staff/{staffId}/{agencyId}/roles/POM",
      1234,
      "MDI",
      response = true,
    )

    val client = PrisonApiClient(prisonApiWebClient)
    val result = client.hasPomRole(1234, "MDI")

    Assertions.assertThat(result).isTrue
  }
}
