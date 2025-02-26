package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ParoleReviewService(
  private val repository: ParoleReviewRepository,
) {
  @Transactional
  fun getParoleReviews(): List<ParoleReview> = repository.findAll()
}
