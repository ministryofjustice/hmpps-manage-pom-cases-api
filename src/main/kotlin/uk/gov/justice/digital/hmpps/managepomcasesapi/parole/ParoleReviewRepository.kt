package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId

@Repository
interface ParoleReviewRepository : JpaRepository<ParoleReview, Long> {
  fun findByCaseIdIn(caseIds: List<NomisId>): List<ParoleReview>
}
