package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.parole.parolecasesservice

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocatedCase
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocatedCasesService
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocationHistory
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.MpcCases
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleCasesService
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleReview
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleReviewRepository
import java.time.LocalDate

class UpcomingParoleCasesTest {

  private val cases = listOf(
    CaseData(prisonerNumber = "ABC123"),
    CaseData(prisonerNumber = "ABC456"),
  )

  private val mpcCases = mock<MpcCases>()
  private val allocatedCasesService = mock<AllocatedCasesService>()
  private val paroleReviewsRepository = mock<ParoleReviewRepository>()

  private fun givenValues(
    caseResults: List<CaseData> = cases,
    allocationCasesResults: List<AllocatedCase> = listOf(),
    paroleReviewResults: List<ParoleReview> = listOf(),
  ) {
    Mockito.`when`(mpcCases.forPrison("LEI")).thenReturn(caseResults)

    Mockito.`when`(allocatedCasesService.forPrison("LEI"))
      .thenReturn(allocationCasesResults)

    Mockito.`when`(paroleReviewsRepository.findByCaseIdIn(listOf("ABC123", "ABC456")))
      .thenReturn(paroleReviewResults)
  }

  @Test
  fun `Case is not upcoming parole when they have no parole reviews at all`() {
    givenValues(
      allocationCasesResults = listOf(
        AllocatedCase(CaseData(prisonerNumber = "ABC123"), AllocationHistory()),
        AllocatedCase(CaseData(prisonerNumber = "ABC456"), AllocationHistory()),
      ),
      paroleReviewResults = listOf(),
    )

    val results = ParoleCasesService(mpcCases, allocatedCasesService, paroleReviewsRepository).upcomingAt("LEI")
    Assertions.assertEquals(0, results.size)
  }

  @Test
  fun `Case is not upcoming parole when they are not allocated`() {
    givenValues(
      allocationCasesResults = listOf(),
      paroleReviewResults = listOf(
        ParoleReview(caseId = "ABC123", targetHearingDate = LocalDate.now().plusDays(1)),
        ParoleReview(caseId = "ABC456", targetHearingDate = LocalDate.now().plusDays(1)),
      ),
    )

    val results = ParoleCasesService(mpcCases, allocatedCasesService, paroleReviewsRepository).upcomingAt("LEI")
    Assertions.assertEquals(0, results.size)
  }

  @Test
  fun `Case is not upcoming parole when their parole date is not within the next 10 months`() {
    givenValues(
      allocationCasesResults = listOf(
        AllocatedCase(CaseData(prisonerNumber = "ABC123"), AllocationHistory()),
        AllocatedCase(CaseData(prisonerNumber = "ABC456"), AllocationHistory()),
      ),
      paroleReviewResults = listOf(
        ParoleReview(caseId = "ABC123", targetHearingDate = LocalDate.now().minusDays(1)),
        ParoleReview(caseId = "ABC456", targetHearingDate = LocalDate.now().minusDays(1)),
      ),
    )

    val results = ParoleCasesService(mpcCases, allocatedCasesService, paroleReviewsRepository).upcomingAt("LEI")
    Assertions.assertEquals(0, results.size)
  }

  @Test
  fun `Case is upcoming parole when they are allocated and their parole review date is in the next 10 months`() {
    givenValues(
      allocationCasesResults = listOf(
        AllocatedCase(CaseData(prisonerNumber = "ABC123"), AllocationHistory()),
        AllocatedCase(CaseData(prisonerNumber = "ABC456"), AllocationHistory()),
      ),
      paroleReviewResults = listOf(
        ParoleReview(caseId = "ABC123", targetHearingDate = LocalDate.now().plusDays(1)),
        ParoleReview(caseId = "ABC456", targetHearingDate = LocalDate.now().minusDays(1)),
      ),
    )

    val results = ParoleCasesService(mpcCases, allocatedCasesService, paroleReviewsRepository).upcomingAt("LEI")

    Assertions.assertEquals(1, results.size)
    Assertions.assertEquals("ABC123", results.first().caseId)
  }
}
