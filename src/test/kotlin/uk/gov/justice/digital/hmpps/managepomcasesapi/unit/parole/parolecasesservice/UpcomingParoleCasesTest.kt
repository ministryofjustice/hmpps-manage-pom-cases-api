package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.parole.parolecasesservice

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.Allocation
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocationsService
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.MpcCasesService
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleCasesService
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleReview
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleReviewRepository
import uk.gov.justice.digital.hmpps.managepomcasesapi.unit.parole.support.DummyAllocation
import java.time.LocalDate

class UpcomingParoleCasesTest {
  private val defaultCases = listOf(
    CaseData(prisonerNumber = "ABC123"),
    CaseData(prisonerNumber = "ABC456"),
  )

  private val mpcCasesService = mock<MpcCasesService>()
  private val allocationsService = mock<AllocationsService>()
  private val paroleReviewsRepository = mock<ParoleReviewRepository>()

  @Test
  fun `Case is not upcoming parole when they are not allocated`() {
    givenValues(
      allocations = listOf(),
      paroleReviews = listOf(
        ParoleReview(caseId = "ABC123", targetHearingDate = LocalDate.now().plusDays(1)),
        ParoleReview(caseId = "ABC456", targetHearingDate = LocalDate.now().plusDays(1)),
      ),
    )

    val results = ParoleCasesService(mpcCasesService, allocationsService, paroleReviewsRepository)
      .upcomingAt("LEI")

    Assertions.assertEquals(0, results.size)
  }

  @Test
  fun `Case is not upcoming parole when their parole date is not within the next 10 months`() {
    givenValues(
      allocations = listOf(
        allocationOf(caseId = "ABC123"),
        allocationOf(caseId = "ABC456"),
      ),
      paroleReviews = listOf(
        ParoleReview(caseId = "ABC123", targetHearingDate = LocalDate.now().minusDays(1)),
        ParoleReview(caseId = "ABC456", targetHearingDate = LocalDate.now().minusDays(1)),
      ),
    )

    val results = ParoleCasesService(mpcCasesService, allocationsService, paroleReviewsRepository)
      .upcomingAt("LEI")

    Assertions.assertEquals(0, results.size)
  }

  @Test
  fun `Case is upcoming parole when they are allocated and their parole review date is in the next 10 months`() {
    givenValues(
      allocations = listOf(
        allocationOf(caseId = "ABC123"),
        allocationOf(caseId = "ABC456"),
      ),
      paroleReviews = listOf(
        ParoleReview(caseId = "ABC123", targetHearingDate = LocalDate.now().plusDays(1)),
        ParoleReview(caseId = "ABC456", targetHearingDate = LocalDate.now().minusDays(1)),
      ),
    )

    val results = ParoleCasesService(mpcCasesService, allocationsService, paroleReviewsRepository)
      .upcomingAt("LEI")

    Assertions.assertEquals(1, results.size)
    Assertions.assertEquals("ABC123", results.first().caseId)
  }

  @Test
  fun `Case is upcoming parole when they are allocated with no role reviews but their PED is in the next 10 months`() {
    givenValues(
      cases = listOf(
        CaseData(prisonerNumber = "ABC123"),
        CaseData(prisonerNumber = "ABC456", paroleEligibilityDate = LocalDate.now().plusMonths(1)),
      ),
      allocations = listOf(
        allocationOf(caseId = "ABC123"),
        allocationOf(caseId = "ABC456"),
      ),
      paroleReviews = listOf(),
    )

    val results = ParoleCasesService(mpcCasesService, allocationsService, paroleReviewsRepository)
      .upcomingAt("LEI")

    Assertions.assertEquals(1, results.size)
    Assertions.assertEquals("ABC456", results.first().caseId)
  }

  private fun givenValues(
    cases: List<CaseData> = defaultCases,
    allocations: List<Allocation> = listOf(),
    paroleReviews: List<ParoleReview> = listOf(),
  ) {
    Mockito.`when`(mpcCasesService.forPrison("LEI")).thenReturn(cases)

    Mockito.`when`(allocationsService.forCasesAtPrison(cases.map { it.caseId },"LEI"))
      .thenReturn(allocations)

    Mockito.`when`(paroleReviewsRepository.latestReviewsFor(listOf("ABC123", "ABC456")))
      .thenReturn(paroleReviews)
  }

  private fun allocationOf(caseId: NomisId): Allocation = DummyAllocation.withCaseId(caseId)
}
