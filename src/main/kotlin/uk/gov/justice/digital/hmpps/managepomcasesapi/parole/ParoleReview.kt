package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import uk.gov.justice.digital.hmpps.managepomcasesapi.offenders.Offender
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(
  name = "parole_reviews",
  indexes = [
    Index(
      name = "index_parole_reviews_on_review_id_nomis_offender_id",
      columnList = "review_id, nomis_offender_id",
      unique = true,
    ),
  ],
)
class ParoleReview {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ColumnDefault("nextval('parole_reviews_id_seq')")
  @Column(name = "id", nullable = false)
  var id: Long? = null

  @Column(name = "review_id")
  var reviewId: Int? = null

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "nomis_offender_id", referencedColumnName = "nomis_offender_id")
  var offender: Offender? = null

  @Column(name = "target_hearing_date")
  var targetHearingDate: LocalDate? = null

  @Column(name = "custody_report_due")
  var custodyReportDue: LocalDate? = null

  @Column(name = "review_status")
  var reviewStatus: String? = null

  @Column(name = "hearing_outcome")
  var hearingOutcome: String? = null

  @Column(name = "hearing_outcome_received_on")
  var hearingOutcomeReceivedOn: LocalDate? = null

  @NotNull
  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  var createdAt: Instant? = null

  @NotNull
  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  var updatedAt: Instant? = null

  @Column(name = "review_type")
  var reviewType: String? = null
}
