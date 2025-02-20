package uk.gov.justice.digital.hmpps.managepomcasesapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ManagePomCasesApi

fun main(args: Array<String>) {
  runApplication<ManagePomCasesApi>(*args)
}
