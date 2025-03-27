package uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility.CaseResponsibility

class ResponsibilityTest {
  @ParameterizedTest
  @CsvSource(
    delimiter = '|',
    textBlock = """
      Community      | false | true  | true  | false
      CustodyOnly    | true  | false | false | false
      CustodyWithCom | true  | false | false | true""",
  )
  fun `Responsibility and supporting rules`(
    responsibility: String,
    pomResponsible: Boolean,
    pomSupporting: Boolean,
    comResponsible: Boolean,
    comSupporting: Boolean,
  ) {
    val caseResponsibility = CaseResponsibility("N0M1S1D", responsibility)
    assert(caseResponsibility.pomResponsible() == pomResponsible)
    assert(caseResponsibility.pomSupporting() == pomSupporting)
    assert(caseResponsibility.comResponsible() == comResponsible)
    assert(caseResponsibility.comSupporting() == comSupporting)
  }

  @ParameterizedTest
  @CsvSource(
    delimiter = '|',
    textBlock = """
      CustodyOnly    | Responsible
      CustodyWithCom | Responsible
      Community      | Supporting""",
  )
  fun `A Poms role should be displayed as`(responsibility: String, role: String) {
    val caseResponsibility = CaseResponsibility("N0M1S1D", responsibility)
    Assertions.assertEquals(role, caseResponsibility.pomRole())
  }
}
