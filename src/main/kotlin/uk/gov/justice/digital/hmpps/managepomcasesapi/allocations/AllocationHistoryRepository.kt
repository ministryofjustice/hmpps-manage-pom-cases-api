package uk.gov.justice.digital.hmpps.managepomcasesapi.allocations

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AllocationHistoryRepository : JpaRepository<AllocationHistory, Long> {

  @Query(
    """
    SELECT * FROM allocation_history 
    WHERE primary_pom_nomis_id IS NOT NULL 
    AND nomis_offender_id IN :caseIds 
    AND prison = :prisonCode""",
    nativeQuery = true,
  )
  fun activeAllocationsAt(prisonCode: String, caseIds: List<String> = emptyList()): List<AllocationHistory>
}
