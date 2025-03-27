package uk.gov.justice.digital.hmpps.managepomcasesapi.parole.repository

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleReviewRepository

@Sql(
  "classpath:test_data/reset.sql",
  "classpath:test_data/parole/some_parole_reviews.sql",
)
class LatestReviewsTest : IntegrationTestBase() {
  @Autowired
  private lateinit var repository: ParoleReviewRepository

  @Test
  fun `Returns the latest parole review ordered by either target_hearing_date or custody_report_due for the given case ids`() {
    val results = repository.latestReviewsFor(listOf("GAX123", "GAX456"))
    Assertions.assertEquals(2, results.count())
    Assertions.assertTrue(results.map { it.id }.contains(1))
    Assertions.assertTrue(results.map { it.id }.contains(3))
  }
}
