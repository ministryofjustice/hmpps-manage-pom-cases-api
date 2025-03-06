package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId
import java.time.LocalDate

data class UpcomingParoleCase(
  val caseId: NomisId,
  val caseName: String,
  val pomName: String?,
  val pomRole: String?,
  val paroleDateValue: LocalDate?,
  val paroleDateType: String?,
)
