package uk.gov.justice.digital.hmpps.managepomcasesapi.offenders

import org.springframework.stereotype.Service

@Service
class OffenderService(
  private val repository: OffenderRepository,
)
