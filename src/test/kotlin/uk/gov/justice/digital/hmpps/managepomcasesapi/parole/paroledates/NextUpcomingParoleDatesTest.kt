package uk.gov.justice.digital.hmpps.managepomcasesapi.parole.paroledates

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleDate
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleDates
import java.time.LocalDate

class NextUpcomingParoleDatesTest {
  @ParameterizedTest
  @CsvSource(
    delimiter = '|',
    textBlock = """
       1 | -1 | -1 | Target Hearing Date     | THD is the only date within the next 10 months
      -1 |  1 | -1 | Tariff End Date         | TED is the only date within the next 10 months
      -1 | -1 |  1 | Parole Eligibility Date | PED is the only date within the next 10 months
       1 | 10 | 10 | Target Hearing Date     | THD is the earliest valid date
      10 |  1 | 10 | Tariff End Date         | PED is the earliest valid date
      10 | 10 |  1 | Parole Eligibility Date | PED is the earliest valid date""",
  )
  fun `Next upcoming date is the earliest date that is in the next 10 months`(thd: Long, ted: Long, ped: Long, expectedType: String, reason: String) {
    val nextUpcomingDate = ParoleDates(
      listOf(
        ParoleDate("Target Hearing Date", LocalDate.now().plusMonths(thd)),
        ParoleDate("Tariff End Date", LocalDate.now().plusMonths(ted)),
        ParoleDate("Parole Eligibility Date", LocalDate.now().plusMonths(ped)),
      ),
    ).nextUpcomingDate()
    Assertions.assertNotNull(nextUpcomingDate)
    Assertions.assertEquals(expectedType, nextUpcomingDate?.type)
  }

  @Test
  fun `Next upcoming date is empty when there are no valid dates`() {
    val nextUpcomingDate = ParoleDates(
      listOf(
        ParoleDate("Target Hearing Date", null),
        ParoleDate("Tariff End Date", LocalDate.now().plusMonths(12)),
        ParoleDate("Parole Eligibility Date", LocalDate.now().plusMonths(12)),
      ),
    ).nextUpcomingDate()
    Assertions.assertNull(nextUpcomingDate)
  }
}
