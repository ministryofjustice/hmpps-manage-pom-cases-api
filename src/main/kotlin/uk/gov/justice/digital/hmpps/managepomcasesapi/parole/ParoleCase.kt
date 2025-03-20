package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId
import java.time.LocalDate

data class ParoleCase(
  val caseId: NomisId,
  val firstName: String?,
  val lastName: String?,
  val pomId: Int?,
  val pomFirstName: String?,
  val pomLastName: String?,
  val pomRole: String?,
  val paroleDateValue: LocalDate?,
  val paroleDateType: String?,
)
