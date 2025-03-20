package uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId

@Repository
interface CalculatedHandoverDatesRepository : JpaRepository<CalculatedHandoverDate, Long> {
  @Query(
    """
      SELECT nomis_offender_id as caseId, responsibility
      FROM calculated_handover_dates
      WHERE nomis_offender_id IN (:caseIds)
    """,
    nativeQuery = true,
  )
  fun responsibilitiesOf(caseIds: List<NomisId>): List<CaseResponsibility>
}
