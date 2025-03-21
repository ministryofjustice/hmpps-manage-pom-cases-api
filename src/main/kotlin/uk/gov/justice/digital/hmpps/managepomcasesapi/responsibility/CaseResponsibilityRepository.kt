package uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId

@Repository
interface CaseResponsibilityRepository : JpaRepository<CalculatedHandoverDate, Long> {
  @Query(
    """
      SELECT
        calculated.nomis_offender_id,
        CASE
          WHEN overridden."value" = 'Probation' THEN 'Community'
          WHEN overridden."value" = 'Prison'    THEN 'CustodyOnly'
          ELSE calculated.responsibility
        END AS responsibility
      FROM calculated_handover_dates AS calculated
      LEFT JOIN responsibilities AS overridden
      ON overridden.nomis_offender_id = calculated.nomis_offender_id
      WHERE calculated.nomis_offender_id IN (:caseIds)
    """,
    nativeQuery = true,
  )
  fun responsibilitiesOf(caseIds: List<NomisId>): List<CaseResponsibility>
}
