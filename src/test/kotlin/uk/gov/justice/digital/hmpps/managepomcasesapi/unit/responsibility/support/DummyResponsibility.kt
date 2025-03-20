package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.responsibility.support

import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId
import uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility.CaseResponsibility

class DummyResponsibility {
  companion object {
    fun pomResponsible(caseId: NomisId) = CaseResponsibility(caseId, "CustodyOnly")
    fun comResponsible(caseId: NomisId) = CaseResponsibility(caseId, "Community")
    fun pomSupporting(caseId: NomisId) = CaseResponsibility(caseId, "Community")
  }
}
