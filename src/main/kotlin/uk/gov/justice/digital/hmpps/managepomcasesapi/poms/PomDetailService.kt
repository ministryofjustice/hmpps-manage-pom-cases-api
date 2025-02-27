package uk.gov.justice.digital.hmpps.managepomcasesapi.poms

import org.springframework.stereotype.Service

@Service
class PomDetailService(
  private val repository: PomDetailRepository,
)
