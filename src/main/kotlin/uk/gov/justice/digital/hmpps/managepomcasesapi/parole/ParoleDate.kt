package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.CaseData
import java.time.LocalDate

class ParoleDate(val type: String, val date: LocalDate?) {
  fun upcoming() = !(
    date == null ||
      date.isBefore(LocalDate.now()) ||
      date.isAfter(LocalDate.now().plusMonths(10))
    )
}

class ParoleDates(private val paroleDates: List<ParoleDate>) {
  fun nextUpcomingDate() = paroleDates
    .filter { it.upcoming() }
    .sortedBy { it.date }
    .firstOrNull()

  companion object {
    fun from(case: CaseData?, paroleReview: ParoleReview?): ParoleDates = ParoleDates(
      listOf(
        ParoleDate("Target Hearing Date", paroleReview?.targetHearingDate),
        ParoleDate("Tariff End Date", case?.tariffDate),
        ParoleDate("Parole Eligibility Date", case?.paroleEligibilityDate),
      ),
    )
  }
}
