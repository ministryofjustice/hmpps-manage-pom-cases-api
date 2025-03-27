package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.parole.parolecasesservice

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.Allocation
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocationsService
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.MpcCasesService
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleCasesService
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleReview
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleReviewRepository
import uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility.CaseResponsibility
import uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility.ResponsibilityService
import uk.gov.justice.digital.hmpps.managepomcasesapi.unit.parole.factory.allocation
import uk.gov.justice.digital.hmpps.managepomcasesapi.unit.responsibility.factory.pomResponsibility
import java.time.LocalDate

class UpcomingParoleCasesTest {
  private val defaultCases = listOf(
    CaseData(prisonerNumber = "ABC123"),
    CaseData(prisonerNumber = "ABC456"),
  )

  private val mpcCasesService = mock<MpcCasesService>()
  private val allocationsService = mock<AllocationsService>()
  private val paroleReviewsRepository = mock<ParoleReviewRepository>()
  private val responsibilityService = mock<ResponsibilityService>()

  @Test
  fun `Case is not upcoming parole when they are not allocated`() {
    givenValues(
      allocations = listOf(),
      paroleReviews = listOf(
        ParoleReview(caseId = "ABC123", targetHearingDate = LocalDate.now().plusDays(1)),
        ParoleReview(caseId = "ABC456", targetHearingDate = LocalDate.now().plusDays(1)),
      ),
      caseResponsibilities = listOf(
        pomResponsibility("ABC123"),
      ),
    )

    val results = ParoleCasesService(mpcCasesService, allocationsService, paroleReviewsRepository, responsibilityService)
      .upcomingAt("LEI")

    Assertions.assertEquals(0, results.size)
  }

  @Test
  fun `Case is not upcoming parole when their parole date is not within the next 10 months`() {
    givenValues(
      allocations = listOf(
        allocation(caseId = "ABC123"),
        allocation(caseId = "ABC456"),
      ),
      paroleReviews = listOf(
        ParoleReview(caseId = "ABC123", targetHearingDate = LocalDate.now().minusDays(1)),
        ParoleReview(caseId = "ABC456", targetHearingDate = LocalDate.now().minusDays(1)),
      ),
      caseResponsibilities = listOf(
        pomResponsibility("ABC123"),
      ),
    )

    val results = ParoleCasesService(mpcCasesService, allocationsService, paroleReviewsRepository, responsibilityService)
      .upcomingAt("LEI")

    Assertions.assertEquals(0, results.size)
  }

  @Test
  fun `Case is upcoming parole when they are allocated and their parole review date is in the next 10 months`() {
    givenValues(
      allocations = listOf(
        allocation(caseId = "ABC123"),
        allocation(caseId = "ABC456"),
      ),
      paroleReviews = listOf(
        ParoleReview(caseId = "ABC123", targetHearingDate = LocalDate.now().plusDays(1)),
        ParoleReview(caseId = "ABC456", targetHearingDate = LocalDate.now().minusDays(1)),
      ),
      caseResponsibilities = listOf(
        pomResponsibility("ABC123"),
      ),
    )

    val results = ParoleCasesService(mpcCasesService, allocationsService, paroleReviewsRepository, responsibilityService)
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
        allocation(caseId = "ABC123"),
        allocation(caseId = "ABC456"),
      ),
      paroleReviews = listOf(),
      caseResponsibilities = listOf(
        pomResponsibility("ABC456"),
      ),
    )

    val results = ParoleCasesService(mpcCasesService, allocationsService, paroleReviewsRepository, responsibilityService)
      .upcomingAt("LEI")

    Assertions.assertEquals(1, results.size)
    Assertions.assertEquals("ABC456", results.first().caseId)
  }

  @Test
  fun `Upcoming parole cases contain basic information regarding the case`() {
    givenValues(
      cases = listOf(
        CaseData(prisonerNumber = "ABC456", firstName = "First", lastName = "Case", paroleEligibilityDate = LocalDate.now().plusMonths(1)),
      ),
      allocations = listOf(
        allocation(caseId = "ABC456", pomId = 999, pomFirstName = "Angela", pomLastName = "Pomme"),
      ),
      caseResponsibilities = listOf(
        pomResponsibility("ABC456"),
      ),
    )

    val results = ParoleCasesService(mpcCasesService, allocationsService, paroleReviewsRepository, responsibilityService)
      .upcomingAt("LEI")

    Assertions.assertEquals(1, results.size)
    with(results.first()) {
      Assertions.assertEquals("ABC456", caseId)
      Assertions.assertEquals("First", firstName)
      Assertions.assertEquals("Case", lastName)
      Assertions.assertEquals(999, pomId)
      Assertions.assertEquals("Angela", pomFirstName)
      Assertions.assertEquals("Pomme", pomLastName)
      Assertions.assertEquals("Responsible", pomRole)
      Assertions.assertEquals("Parole Eligibility Date", paroleDateType)
      Assertions.assertEquals(LocalDate.now().plusMonths(1), paroleDateValue)
    }
  }

  private fun givenValues(
    cases: List<CaseData> = defaultCases,
    allocations: List<Allocation> = listOf(),
    paroleReviews: List<ParoleReview> = listOf(),
    caseResponsibilities: List<CaseResponsibility> = listOf(),
  ) {
    val caseIds = cases.map { it.caseId }

    Mockito.`when`(mpcCasesService.forPrison("LEI")).thenReturn(cases)

    Mockito.`when`(allocationsService.forCasesAtPrison(caseIds, "LEI"))
      .thenReturn(allocations)

    Mockito.`when`(paroleReviewsRepository.latestReviewsFor(caseIds))
      .thenReturn(paroleReviews)

    Mockito.`when`(responsibilityService.responsibilityOf(caseIds))
      .thenReturn(caseResponsibilities)
  }
}
