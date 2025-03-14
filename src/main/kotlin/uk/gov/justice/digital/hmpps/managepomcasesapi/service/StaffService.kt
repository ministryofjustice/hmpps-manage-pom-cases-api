package uk.gov.justice.digital.hmpps.managepomcasesapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.client.PrisonApiClient

@Service
class StaffService(
  private val prisonApiClient: PrisonApiClient,
) {
  fun staffDetail(staffId: Int) = prisonApiClient.staffDetail(staffId)
  fun hasPomRole(staffId: Int, agencyId: String) = prisonApiClient.hasPomRole(staffId, agencyId)
}
