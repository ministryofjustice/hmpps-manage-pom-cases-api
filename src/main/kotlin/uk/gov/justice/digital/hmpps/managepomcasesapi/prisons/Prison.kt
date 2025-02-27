package uk.gov.justice.digital.hmpps.managepomcasesapi.prisons

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import uk.gov.justice.digital.hmpps.managepomcasesapi.poms.PomDetail
import java.time.Instant

enum class PrisonType(val type: String) {
  WOMENS("womens"),
  MENS_OPEN("mens_open"),
  MENS_CLOSED("mens_closed"),
}

@Entity
@Table(
  name = "prisons",
  indexes = [
    Index(name = "index_prisons_on_name", columnList = "name", unique = true),
  ],
)
class Prison {
  @Id
  @Column(name = "code", nullable = false)
  var code: String? = null

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "prison_type", nullable = false)
  var prisonType: PrisonType? = null

  @NotNull
  @Column(name = "name", nullable = false)
  var name: String? = null

  @NotNull
  @Column(name = "created_at", nullable = false)
  var createdAt: Instant? = null

  @NotNull
  @Column(name = "updated_at", nullable = false)
  var updatedAt: Instant? = null

  @OneToMany(mappedBy = "prison", fetch = FetchType.LAZY)
  var pomDetails: MutableList<PomDetail> = mutableListOf()
}
