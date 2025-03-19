package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.parole.support

import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.Allocation
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId

class DummyAllocation {
  companion object {
    fun withCaseId(caseId: NomisId): Allocation = Allocation(
      caseId = caseId,
      pomId = -1,
      pomFirstName = "_FIRST_NAME",
      pomLastName = "_LAST_NAME",
    )
  }
}
