package uk.gov.justice.digital.hmpps.managepomcasesapi.cases

import java.time.LocalDate

class CaseData(
  // Ids

  val prisonerNumber: String,
  val bookingId: String,
  val prisonId: String,
  val supportingPrisonId: String,
  val dischargedHospitalId: String,

  // Personal Details

  val title: String,
  val firstName: String,
  val lastName: String,
  val dateOfBirth: LocalDate,
  val gender: String,

  // Status flags

  val status: String,
  val inOutStatus: String,
  val category: String,
  val legalStatus: String,
  val imprisonmentStatus: String,
  val imprisonmentStatusDescription: String,
  val convictedStatus: String,
  val recall: Boolean,
  val indeterminateSentence: Boolean,
  val restrictedPatient: Boolean,

  // Offence details

  val mostSeriousOffence: String,

  // Location / Movements

  val lastMovementTypeCode: String,
  val lastMovementReasonCode: String,
  val cellLocation: String,
  val locationDescription: String,

  // Sentence Dates

  val sentenceStartDate: LocalDate,
  val releaseDate: LocalDate,
  val confirmedReleaseDate: LocalDate,
  val sentenceExpiryDate: LocalDate,
  val licenceExpiryDate: LocalDate,
  val homeDetentionCurfewEligibilityDate: LocalDate,
  val homeDetentionCurfewActualDate: LocalDate,
  val homeDetentionCurfewEndDate: LocalDate,
  val receptionDate: LocalDate,
  val paroleEligibilityDate: LocalDate,
  val automaticReleaseDate: LocalDate,
  val postRecallReleaseDate: LocalDate,
  val conditionalReleaseDate: LocalDate,
  val actualParoleDate: LocalDate,
  val tariffDate: LocalDate,
  val releaseOnTemporaryLicenceDate: LocalDate,
  val dischargeDate: LocalDate,
)
