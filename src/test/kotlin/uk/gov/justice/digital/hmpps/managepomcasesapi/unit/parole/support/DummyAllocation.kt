package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.parole.support

import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.Allocation
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId

class DummyAllocation {
  companion object {
    fun with(
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
  }
}
