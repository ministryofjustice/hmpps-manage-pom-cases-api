package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.managepomcasesapi.allocations.AllocationsService
import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.MpcCasesService
import uk.gov.justice.digital.hmpps.managepomcasesapi.responsibility.ResponsibilityService

@Service
class ParoleCasesService(
  private val mpcCasesService: MpcCasesService,
  private val allocationsService: AllocationsService,
  private val paroleReviewsRepository: ParoleReviewRepository,
  private val responsibilityService: ResponsibilityService,
) {
  fun upcomingAt(prisonCode: String): List<ParoleCase> {
    with(ParoleCasesBuilder()) {
      mpcCasesService.forPrison(prisonCode).forEach(::addCase)
      allocationsService.forCasesAtPrison(caseIds, prisonCode).forEach(::addAllocation)
      paroleReviewsRepository.latestReviewsFor(allocatedCaseIds).forEach(::addParoleReview)
      responsibilityService.responsibilityOf(allocatedCaseIds).forEach(::addResponsibility)
      return upcomingParoleCases()
    }
  }
}
