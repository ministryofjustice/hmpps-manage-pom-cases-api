package uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId

@Service
class ResponsibilityService(
  private val calculatedHandoverDatesRepository: CalculatedHandoverDatesRepository,
) {
  fun responsibilityOf(caseIds: List<NomisId>): List<CaseResponsibility> = calculatedHandoverDatesRepository
    .responsibilitiesOf(caseIds)
}
