package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParoleReviewRepository : JpaRepository<ParoleReview, Long>
