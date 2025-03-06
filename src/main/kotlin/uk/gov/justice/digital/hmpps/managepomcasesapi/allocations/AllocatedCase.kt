package uk.gov.justice.digital.hmpps.managepomcasesapi.allocations

import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData

class AllocatedCase(caseData: CaseData, allocationHistory: AllocationHistory) {
  val caseId = caseData.caseId
  val pomName = allocationHistory.primaryPomName
}
