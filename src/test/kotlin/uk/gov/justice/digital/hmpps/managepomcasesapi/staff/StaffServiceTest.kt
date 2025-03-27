package uk.gov.justice.digital.hmpps.managepomcasesapi.staff

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.managepomcasesapi.client.PrisonApiClient

@ExtendWith(MockitoExtension::class)
class StaffServiceTest {
  @Mock
  lateinit var prisonApiClient: PrisonApiClient

  @InjectMocks
  lateinit var service: StaffService

  @Test
  fun `Checking if a staff member has POM role in a prison`() {
    whenever(prisonApiClient.hasPomRole(1234, "MDI")).thenReturn(true)
    assert(service.hasPomRole(1234, "MDI"))
  }
}
