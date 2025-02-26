package uk.gov.justice.digital.hmpps.managepomcasesapi.offenders

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OffenderRepository : JpaRepository<Offender, String>
