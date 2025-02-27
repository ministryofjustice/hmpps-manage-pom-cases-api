package uk.gov.justice.digital.hmpps.managepomcasesapi.prisons

import org.springframework.stereotype.Service

@Service
class PrisonService(
  private val repository: PrisonRepository,
)
