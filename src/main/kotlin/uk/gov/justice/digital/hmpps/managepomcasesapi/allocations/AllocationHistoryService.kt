package uk.gov.justice.digital.hmpps.managepomcasesapi.allocations

import org.springframework.stereotype.Service

@Service
class AllocationHistoryService(
  private val repository: AllocationHistoryRepository,
)
