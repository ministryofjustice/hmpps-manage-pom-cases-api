package uk.gov.justice.digital.hmpps.managepomcasesapi.allocations

import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData

class AllocatedCase(caseData: CaseData, allocationHistory: AllocationHistory) {
  val caseId = caseData.caseId
  val pomId = allocationHistory.primaryPomNomisId
  val pomFirstName = allocationHistory.primaryPomName?.split(" ")?.first()
  val pomLastName = allocationHistory.primaryPomName?.split(" ")?.last()
}
