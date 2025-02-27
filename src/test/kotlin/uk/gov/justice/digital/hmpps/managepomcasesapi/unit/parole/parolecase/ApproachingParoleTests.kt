package uk.gov.justice.digital.hmpps.managepomcasesapi.unit.parole.parolecase

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleCase
import java.time.LocalDate

class ApproachingParoleTests {

  @Test
  fun `Not approaching parole when neither THD, TED or PED are set`() {
    val paroleCase = ParoleCase()
    assert(!paroleCase.approachingParole())
  }

  @ParameterizedTest
  @CsvSource(
    delimiter = '|',
    textBlock = """
      targetHearingDate     | 365 | THD too far in the future
      targetHearingDate     | -1  | THD in the past
      paroleEligibilityDate | 365 | PED too far in the future
      paroleEligibilityDate | -1  | PED in the past
      tariffEndDate         | 365 | TED too far in the future
      tariffEndDate         | -1  | TED in the past""",
  )
  fun `Not approaching parole when THD, TED or PED are present but are not within the next 10 months`(field: String, daysFromNow: Long, explanation: String) {
    val dates = hashMapOf<String, LocalDate?>(
      "targetHearingDate" to null,
      "tariffEndDate" to null,
      "paroleEligibilityDate" to null,
    )
    dates[field] = LocalDate.now().plusDays(daysFromNow)

    val paroleCase = ParoleCase(
      dates["targetHearingDate"],
      dates["paroleEligibilityDate"],
      dates["tariffEndDate"],
    )
    assert(!paroleCase.approachingParole())
  }

  @ParameterizedTest
  @CsvSource(
    delimiter = '|',
    textBlock = """
      targetHearingDate     | 0  | THD is today
      targetHearingDate     | 10 | THD is 10 months in the future
      targetHearingDate     | 5  | THD is 5 months in the future
      tariffEndDate         | 0  | TED is today
      tariffEndDate         | 10 | TED is 10 months in the future
      tariffEndDate         | 5  | TED is 5 months in the future
      paroleEligibilityDate | 0  | PED is today
      paroleEligibilityDate | 10 | PED is 10 months in the future
      paroleEligibilityDate | 5  | PED is 5 months in the future""",
  )
  fun `Approaching parole when one of THD, TED or PED is within the next 10 months`(field: String, monthsFromNow: Long, explanation: String) {
    val dates = hashMapOf<String, LocalDate?>(
      "targetHearingDate" to null,
      "tariffEndDate" to null,
      "paroleEligibilityDate" to null,
    )
    dates[field] = LocalDate.now().plusMonths(monthsFromNow)

    val paroleCase = ParoleCase(
      dates["targetHearingDate"],
      dates["paroleEligibilityDate"],
      dates["tariffEndDate"],
    )
    assert(paroleCase.approachingParole())
  }
}
