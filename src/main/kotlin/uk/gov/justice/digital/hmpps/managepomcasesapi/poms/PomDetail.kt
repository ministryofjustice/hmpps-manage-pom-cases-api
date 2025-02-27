package uk.gov.justice.digital.hmpps.managepomcasesapi.poms

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
import uk.gov.justice.digital.hmpps.managepomcasesapi.prisons.Prison
import java.time.Instant

enum class StatusType(val status: String) {
  ACTIVE("active"),
  INACTIVE("inactive"),
  UNAVAILABLE("unavailable"),
}

@Entity
@Table(
  name = "pom_details",
  indexes = [
    Index(
      name = "index_pom_details_on_nomis_staff_id_and_prison_code",
      columnList = "nomis_staff_id, prison_code",
      unique = true,
    ),
  ],
)
class PomDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ColumnDefault("nextval('pom_details_id_seq')")
  @Column(name = "id", nullable = false)
  var id: Long? = null

  @Column(name = "nomis_staff_id")
  var nomisStaffId: Int? = null

  @Column(name = "working_pattern")
  var workingPattern: Double? = null

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  var status: StatusType? = null

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "prison_code", referencedColumnName = "code")
  var prison: Prison? = null

  @NotNull
  @Column(name = "created_at", nullable = false)
  var createdAt: Instant? = null

  @NotNull
  @Column(name = "updated_at", nullable = false)
  var updatedAt: Instant? = null
}
