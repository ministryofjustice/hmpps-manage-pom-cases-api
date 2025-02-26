package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.managepomcasesapi.service.StaffService
import uk.gov.justice.digital.hmpps.managepomcasesapi.support.StubbedRequests

class StaffServiceTest {
  private val prisonApiWebClient = mock<WebClient>()
  private val stubbedRequests = StubbedRequests(prisonApiWebClient)

  @Test
  fun `Retrieving details for a given staff id`() {
    stubbedRequests.get(
      "/api/staff/{staffId}",
      1234,
      response = StaffService.StaffDetail(
        staffId = 1234,
        firstName = "Jane",
        lastName = "Smith",
        status = "ACTIVE",
      ),
    )

    val staffService = StaffService(prisonApiWebClient)
    val result = staffService.staffDetail(1234)

    Assertions.assertThat(result).isNotNull
    Assertions.assertThat(result?.staffId).isEqualTo(1234)
  }
}
