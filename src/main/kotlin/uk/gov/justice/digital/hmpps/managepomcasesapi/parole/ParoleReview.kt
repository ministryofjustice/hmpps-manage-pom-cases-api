package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "parole_reviews")
open class ParoleReview {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parole_reviews_id_gen")
  @SequenceGenerator(name = "parole_reviews_id_gen", sequenceName = "parole_reviews_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  open var id: Long? = null

  @Column(name = "review_id")
  open var reviewId: Int? = null

  @Column(name = "nomis_offender_id")
  open var nomisOffenderId: String? = null

  @Column(name = "target_hearing_date")
  open var targetHearingDate: LocalDate? = null

  @Column(name = "custody_report_due")
  open var custodyReportDue: LocalDate? = null

  @Column(name = "review_status")
  open var reviewStatus: String? = null

  @Column(name = "hearing_outcome")
  open var hearingOutcome: String? = null

  @Column(name = "hearing_outcome_received_on")
  open var hearingOutcomeReceivedOn: LocalDate? = null

  @NotNull
  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  open var createdAt: Instant? = null

  @NotNull
  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  open var updatedAt: Instant? = null

  @Column(name = "review_type")
  open var reviewType: String? = null
}
