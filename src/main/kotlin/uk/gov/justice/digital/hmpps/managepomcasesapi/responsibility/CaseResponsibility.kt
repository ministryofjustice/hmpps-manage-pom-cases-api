package uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility

import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId

class CaseResponsibility(val caseId: NomisId, val responsibility: String) {
  fun pomResponsible() = listOf("CustodyOnly", "CustodyWithCom").contains(responsibility)
  fun pomSupporting() = responsibility == "Community"
  fun comResponsible() = responsibility == "Community"
  fun comSupporting() = responsibility == "CustodyWithCom"

  fun pomRole() = if (pomResponsible()) {
    "Responsible"
  } else {
    "Supporting"
  }
}
