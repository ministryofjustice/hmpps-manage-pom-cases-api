package uk.gov.justice.digital.hmpps.managepomcasesapi.allocations

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId

@Service
class AllocationsService(
  private val allocationHistoryRepository: AllocationHistoryRepository,
) {
  fun forCasesAtPrison(caseIds: List<NomisId>, prisonCode: String): List<Allocation> = allocationHistoryRepository
    .activeAllocationsAt(prisonCode, caseIds).map {
      Allocation(
        caseId = it.caseId!!,
        pomId = it.primaryPomNomisId!!,
        // TODO: rethink this...
        pomFirstName = it.primaryPomName?.split(", ")?.last(),
        pomLastName = it.primaryPomName?.split(", ")?.first(),
      )
    }
}
