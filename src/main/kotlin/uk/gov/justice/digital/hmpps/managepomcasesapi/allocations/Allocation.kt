package uk.gov.justice.digital.hmpps.managepomcasesapi.allocations

class Allocation(
  val caseId: String,
  val pomId: Int,
  val pomFirstName: String? = null,
  val pomLastName: String? = null,
)
