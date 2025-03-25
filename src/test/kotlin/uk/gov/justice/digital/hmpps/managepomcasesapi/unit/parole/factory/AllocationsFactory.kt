package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.parole.factory

import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.Allocation
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId

fun allocation(
  caseId: NomisId,
  pomId: Int = -1,
  pomFirstName: String = "_FIRST_NAME",
  pomLastName: String = "_LAST_NAME",
): Allocation = Allocation(
  caseId,
  pomId,
  pomFirstName,
  pomLastName,
)
