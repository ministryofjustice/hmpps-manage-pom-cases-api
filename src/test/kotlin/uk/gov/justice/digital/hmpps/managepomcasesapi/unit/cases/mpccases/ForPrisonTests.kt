package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.cases.mpccases

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.MpcCases
import uk.gov.justice.digital.hmpps.managepomcasesapi.service.PrisonerSearchService

class ForPrisonTests {

  @Test
  fun `Cases without a valid legalStatus are excluded`() {
    val prisonerSearchService = mock<PrisonerSearchService>()
    Mockito.`when`(prisonerSearchService.findByPrison("LEI")).thenReturn(
      listOf(
        CaseData(prisonerNumber = "ABC123", legalStatus = "SENTENCED"),
        CaseData(prisonerNumber = "ABC456", legalStatus = "INDETERMINATE_SENTENCE"),
        CaseData(prisonerNumber = "ABC789", legalStatus = "RECALL"),
        CaseData(prisonerNumber = "DEF123", legalStatus = "IMMIGRATION_DETAINEE"),
        CaseData(prisonerNumber = "DEF456", legalStatus = "INVALID"),
      )
    )

    val results = MpcCases(prisonerSearchService).forPrison("LEI")
    Assertions.assertEquals(results.size, 4)
    Assertions.assertEquals(results.map { it.prisonerNumber }, listOf("ABC123", "ABC456", "ABC789", "DEF123"))
  }

  @Test
  fun `Cases with a valid legalStatus but an imprisonmentStatus of A_FINE are excluded`() {
    val prisonerSearchService = mock<PrisonerSearchService>()
    Mockito.`when`(prisonerSearchService.findByPrison("LEI")).thenReturn(
      listOf(
        CaseData(prisonerNumber = "ABC123", imprisonmentStatus = "A_FINE", legalStatus = "SENTENCED"),
        CaseData(prisonerNumber = "ABC456", legalStatus = "SENTENCED"),
        CaseData(prisonerNumber = "ABC789", legalStatus = "SENTENCED"),
      )
    )

    val results = MpcCases(prisonerSearchService).forPrison("LEI")
    Assertions.assertEquals(results.size, 2)
    Assertions.assertEquals(results.map { it.prisonerNumber }, listOf("ABC456", "ABC789"))
  }

}