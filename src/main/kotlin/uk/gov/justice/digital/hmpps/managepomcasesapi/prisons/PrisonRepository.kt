package uk.gov.justice.digital.hmpps.managepomcasesapi.prisons

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PrisonRepository : JpaRepository<Prison, String>
