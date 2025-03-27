package uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility.factory

import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId
import uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility.CaseResponsibility

fun caseResponsibility(caseId: NomisId, responsibility: String) = CaseResponsibility(caseId, responsibility)

fun pomResponsibility(caseId: NomisId) = caseResponsibility(caseId, "CustodyOnly")
fun comResponsibility(caseId: NomisId) = caseResponsibility(caseId, "Community")
