package uk.gov.justice.digital.hmpps.managepomcasesapi.allocations

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

enum class TierType(val tier: String) {
  A("A"),
  B("B"),
  C("C"),
  D("D"),
}

enum class EventType {
  ALLOCATE_PRIMARY_POM,
  REALLOCATE_PRIMARY_POM,
  ALLOCATE_SECONDARY_POM,
  REALLOCATE_SECONDARY_POM,
  DEALLOCATE_PRIMARY_POM,
  DEALLOCATE_SECONDARY_POM,
  DEALLOCATE_RELEASED_OFFENDER,
}

enum class EventTriggerType {
  USER,
  OFFENDER_TRANSFERRED,
  OFFENDER_RELEASED,
}

enum class RecommendedPomType(val recommendation: String) {
  PROBATION("probation"),
  PRISON("prison"),
}

@Entity
@Table(
  name = "allocation_history",
  indexes = [
    Index(name = "index_allocation_history_on_nomis_offender_id", columnList = "nomis_offender_id", unique = true),
    Index(name = "index_allocation_history_on_prison", columnList = "prison"),
    Index(name = "index_allocation_history_on_primary_pom_nomis_id", columnList = "primary_pom_nomis_id"),
    Index(name = "index_allocation_versions_secondary_pom_nomis_id", columnList = "secondary_pom_nomis_id"),
  ],
)
class AllocationHistory(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ColumnDefault("nextval('allocation_history_id_seq')")
  @Column(name = "id", nullable = false)
  var id: Long? = null,

  @Column(name = "nomis_offender_id")
  var caseId: String? = null,

  @Column(name = "prison")
  var prison: String? = null,

  @Enumerated(EnumType.STRING)
  @Column(name = "allocated_at_tier")
  var allocatedAtTier: TierType? = null,

  @Column(name = "override_reasons")
  var overrideReasons: String? = null,

  @Column(name = "override_detail")
  var overrideDetail: String? = null,

  @Column(name = "message")
  var message: String? = null,

  @Column(name = "suitability_detail")
  var suitabilityDetail: String? = null,

  @Column(name = "primary_pom_name")
  var primaryPomName: String? = null,

  @Column(name = "secondary_pom_name")
  var secondaryPomName: String? = null,

  @Column(name = "created_by_name")
  var createdByName: String? = null,

  @Column(name = "primary_pom_nomis_id")
  var primaryPomNomisId: Int? = null,

  @Column(name = "secondary_pom_nomis_id")
  var secondaryPomNomisId: Int? = null,

  @Enumerated
  @Column(name = "event")
  var event: EventType? = null,

  @Enumerated
  @Column(name = "event_trigger")
  var eventTrigger: EventTriggerType? = null,

  @NotNull
  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  var createdAt: Instant? = null,

  @NotNull
  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  var updatedAt: Instant? = null,

  @Column(name = "primary_pom_allocated_at")
  var primaryPomAllocatedAt: Instant? = null,

  @Enumerated(EnumType.STRING)
  @Column(name = "recommended_pom_type")
  var recommendedPomType: RecommendedPomType? = null,
)
