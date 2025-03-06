package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import java.time.LocalDate

class ParoleDate(val type: String, val date: LocalDate?) {
  fun upcoming(): Boolean {
    val now = LocalDate.now()
    val tenMonthsFromNow = now.plusMonths(10)

    return !(date == null || date.isBefore(now) || date.isAfter(tenMonthsFromNow))
  }
}
