package uk.gov.justice.digital.hmpps.managepomcasesapi.staff

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.client.PrisonApiClient

@Service
class StaffService(
  private val prisonApiClient: PrisonApiClient,
) {
  fun staffDetail(staffId: Int) = prisonApiClient.staffDetail(staffId)
  fun hasPomRole(staffId: Int, agencyId: String): Boolean = prisonApiClient.hasPomRole(staffId, agencyId)
}
