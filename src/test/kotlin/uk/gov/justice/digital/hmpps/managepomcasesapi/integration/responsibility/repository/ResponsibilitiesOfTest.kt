package uk.gov.justice.digital.hmpps.managepomcasesapi.integration.responsibility.repository

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility.CaseResponsibilityRepository

@Sql(
  "classpath:test_data/reset.sql",
  "classpath:test_data/responsibility/some_calculated_handover_dates.sql",
  "classpath:test_data/responsibility/some_responsibilities.sql",
)
class ResponsibilitiesOfTest : IntegrationTestBase() {
  @Autowired
  lateinit var calculatedHandoverDatesRepository: CaseResponsibilityRepository

  @Test
  fun `Pulls responsibility from both tables with user entered values taking precedence over calculated ones`() {
    val responsibilities = calculatedHandoverDatesRepository
      .responsibilitiesOf(listOf("GAX007", "GAX456", "GAX123"))
      .associateBy { it.caseId }

    Assertions.assertEquals(3, responsibilities.size)
    Assertions.assertEquals("CustodyOnly", responsibilities["GAX007"]?.responsibility)
    Assertions.assertEquals("Community", responsibilities["GAX456"]?.responsibility)
    Assertions.assertEquals("Community", responsibilities["GAX123"]?.responsibility)
  }
}
