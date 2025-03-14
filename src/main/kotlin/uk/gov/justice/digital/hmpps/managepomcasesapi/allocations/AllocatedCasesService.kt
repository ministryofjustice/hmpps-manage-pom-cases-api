package uk.gov.justice.digital.hmpps.managepomcasesapi.allocations

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.MpcCasesService

@Service
class AllocatedCasesService(
  private val mpcCasesService: MpcCasesService,
  private val allocationHistoryRepository: AllocationHistoryRepository,
) {
  fun forPrison(prisonCode: String): List<AllocatedCase> {
    val cases = mpcCasesService.forPrison(prisonCode).associateBy { it.caseId }

    val activeAllocations = allocationHistoryRepository
      .byCaseIdActiveForPrison(prisonCode, cases.keys.toList())

    return activeAllocations.map { AllocatedCase(cases[it.caseId]!!, it) }
  }
}
