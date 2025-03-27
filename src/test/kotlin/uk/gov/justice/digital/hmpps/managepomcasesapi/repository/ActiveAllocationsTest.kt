package uk.gov.justice.digital.hmpps.managepomcasesapi.repository

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocationHistoryRepository
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.IntegrationTestBase

@Sql(
  "classpath:test_data/reset.sql",
  "classpath:test_data/allocations/some_allocation_histories.sql",
)
class ActiveAllocationsTest : IntegrationTestBase() {
  @Autowired
  private lateinit var repository: AllocationHistoryRepository

  @Test
  fun `Returns allocations with a primary pom defined at the given prison for the given case ids`() {
    val results = repository.activeAllocationsAt("WHI", listOf("GAX911"))
    Assertions.assertEquals(1, results.count())
    Assertions.assertEquals(listOf("GAX911"), results.map { it.caseId })
  }

  @Test
  fun `Does not return allocations where there is no primary POM`() {
    val results = repository.activeAllocationsAt("LEI", listOf("GAX123", "GAX456", "GAX678", "GAX911"))
    Assertions.assertEquals(2, results.count())
    Assertions.assertEquals(listOf("GAX123", "GAX456"), results.map { it.caseId })
  }

  @Test
  fun `Does not return allocations where case id is not in the specified list`() {
    val results = repository.activeAllocationsAt("LEI", listOf("GAX123"))
    Assertions.assertEquals(1, results.count())
    Assertions.assertEquals(listOf("GAX123"), results.map { it.caseId })
  }

  @Test
  fun `Does not return allocations at a different prison to the one that is specified`() {
    val results = repository.activeAllocationsAt("NOT", listOf("GAX123", "GAX456", "GAX678", "GAX911"))
    Assertions.assertEquals(0, results.count())
  }
}
