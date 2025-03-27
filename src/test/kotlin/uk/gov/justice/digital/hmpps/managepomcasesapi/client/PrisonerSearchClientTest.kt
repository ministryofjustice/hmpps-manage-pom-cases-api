package uk.gov.justice.digital.hmpps.managepomcasesapi.client

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.managepomcasesapi.client.PrisonerSearchClient
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock.PrisonerSearchApiExtension.Companion.prisonerSearchApi

class PrisonerSearchClientTest : IntegrationTestBase() {
  @Autowired
  lateinit var prisonerSearchClient: PrisonerSearchClient

  @BeforeEach
  fun setup() {
    hmppsAuth.stubGrantToken()
  }

  @Test
  fun `Searching for prisoners at a given prison returns a list of prisoner details`() {
    prisonerSearchApi.stubFindByPrisonIdResponses(
      """
        { 
          "totalElements": 4, 
          "totalPages": 2, 
          "first": true, 
          "last": false, 
          "content": [${listOf("GAX123", "GAX456", "GAX789").joinToString(",") { """{"prisonerNumber":"$it"}""" }}]
        }
      """.trimMargin(),
      """
        {
          "totalElements": 4,
          "totalPages": 2,
          "first": false,
          "last": true,
          "content": [{"prisonerNumber":"GAX101112"}]
        }
      """.trimMargin(),
      prisonCode = "LEI",
    )

    val results = prisonerSearchClient.findByPrison("LEI")

    Assertions.assertEquals(4, results.size)
    Assertions.assertEquals(listOf("GAX123", "GAX456", "GAX789", "GAX101112"), results.map { it.caseId })
  }
}
