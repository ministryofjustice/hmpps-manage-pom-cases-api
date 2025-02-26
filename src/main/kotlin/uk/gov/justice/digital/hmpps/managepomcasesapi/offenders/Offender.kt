package uk.gov.justice.digital.hmpps.managepomcasesapi.offenders

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocationHistory
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleReview
import java.time.Instant

@Entity
@Table(name = "offenders")
class Offender {
  @Id
  @Column(name = "nomis_offender_id", nullable = false)
  var nomisOffenderId: String? = null

  @NotNull
  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  var createdAt: Instant? = null

  @NotNull
  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  var updatedAt: Instant? = null

  @OrderBy("createdAt DESC")
  @OneToMany(mappedBy = "offender", fetch = FetchType.LAZY, orphanRemoval = true)
  var paroleReviews: MutableList<ParoleReview> = mutableListOf()

  @OrderBy("createdAt DESC")
  @OneToMany(mappedBy = "offender", fetch = FetchType.LAZY)
  var allocations: MutableList<AllocationHistory> = mutableListOf()
}
