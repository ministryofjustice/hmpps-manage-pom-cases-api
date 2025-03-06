package uk.gov.justice.digital.hmpps.managepomcasesapi.cases

import uk.gov.justice.digital.hmpps.managepomcasesapi.cases.types.NomisId
import java.time.LocalDate

class CaseData(
  val prisonerNumber: NomisId = "",
  val bookingId: String = "",
  val prisonId: String = "",
  val supportingPrisonId: String = "",
  val dischargedHospitalId: String = "",

  // Personal Details

  val title: String = "",
  val firstName: String = "",
  val lastName: String = "",
  val dateOfBirth: LocalDate? = null,
  val gender: String = "",

  // Status flags

  val status: String = "",
  val inOutStatus: String = "",
  val category: String = "",
  val legalStatus: String = "",
  val imprisonmentStatus: String = "",
  val imprisonmentStatusDescription: String = "",
  val convictedStatus: String = "",
  val recall: Boolean = false,
  val indeterminateSentence: Boolean = false,
  val restrictedPatient: Boolean = false,

  // Offence details

  val mostSeriousOffence: String = "",

  // Location / Movements

  val lastMovementTypeCode: String = "",
  val lastMovementReasonCode: String = "",
  val cellLocation: String = "",
  val locationDescription: String = "",

  // Sentence Dates

  val sentenceStartDate: LocalDate? = null,
  val releaseDate: LocalDate? = null,
  val confirmedReleaseDate: LocalDate? = null,
  val sentenceExpiryDate: LocalDate? = null,
  val licenceExpiryDate: LocalDate? = null,
  val homeDetentionCurfewEligibilityDate: LocalDate? = null,
  val homeDetentionCurfewActualDate: LocalDate? = null,
  val homeDetentionCurfewEndDate: LocalDate? = null,
  val receptionDate: LocalDate? = null,
  val paroleEligibilityDate: LocalDate? = null,
  val automaticReleaseDate: LocalDate? = null,
  val postRecallReleaseDate: LocalDate? = null,
  val conditionalReleaseDate: LocalDate? = null,
  val actualParoleDate: LocalDate? = null,
  val tariffDate: LocalDate? = null,
  val releaseOnTemporaryLicenceDate: LocalDate? = null,
  val dischargeDate: LocalDate? = null,
) {
  val caseId = prisonerNumber
  val fullName = "$firstName $lastName"
}
