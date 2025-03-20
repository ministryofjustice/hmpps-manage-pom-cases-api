package uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(
  name = "calculated_handover_dates",
)
class CalculatedHandoverDate(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ColumnDefault("nextval('calculated_handover_dates_id_seq')")
  @Column(name = "id", nullable = false)
  var id: Long? = null,

  @Column(name = "nomis_offender_id")
  var caseId: String? = null,

  @Column(name = "handover_date")
  var handoverDate: LocalDate? = null,

  @Column(name = "reason")
  var reason: String? = null,

  @Column(name = "responsibility")
  var responsibility: String? = null,

  @NotNull
  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  var createdAt: Instant? = null,

  @NotNull
  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  var updatedAt: Instant? = null,
)
