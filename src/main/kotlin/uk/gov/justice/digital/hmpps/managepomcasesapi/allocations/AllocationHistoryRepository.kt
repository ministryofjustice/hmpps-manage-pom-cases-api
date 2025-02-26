package uk.gov.justice.digital.hmpps.managepomcasesapi.allocations

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AllocationHistoryRepository : JpaRepository<AllocationHistory, Long>
