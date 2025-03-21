package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId

@Repository
interface ParoleReviewRepository : JpaRepository<ParoleReview, Long> {
  @Query(
    """
      SELECT
        DISTINCT ON (nomis_offender_id)
        COALESCE(target_hearing_date, custody_report_due) AS next_parole_date, *
      FROM parole_reviews
      WHERE NOT (target_hearing_date IS NULL AND custody_report_due IS NULL)
      AND nomis_offender_id IN (:caseIds)
      ORDER BY nomis_offender_id, next_parole_date DESC
    """,
    nativeQuery = true,
  )
  fun latestReviewsFor(caseIds: List<NomisId>): List<ParoleReview>
}
