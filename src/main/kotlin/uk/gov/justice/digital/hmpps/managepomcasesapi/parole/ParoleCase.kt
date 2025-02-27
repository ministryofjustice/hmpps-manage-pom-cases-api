package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import java.time.LocalDate

class ParoleCase(
  val targetHearingDate: LocalDate? = null,
  val tariffEndDate: LocalDate? = null,
  val paroleEligibilityDate: LocalDate? = null,
) {
  fun approachingParole(): Boolean {
    val now = LocalDate.now()
    val tenMonthsFromNow = LocalDate.now().plusMonths(10)

    return listOf(targetHearingDate, tariffEndDate, paroleEligibilityDate)
      .any { !(it == null || it.isBefore(now) || it.isAfter(tenMonthsFromNow)) }
  }
}
