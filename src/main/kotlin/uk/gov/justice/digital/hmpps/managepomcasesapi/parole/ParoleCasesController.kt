package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestController
@PreAuthorize("hasRole('ROLE_MANAGE_POM_CASES__MANAGE_POM_CASES_UI')")
@RequestMapping(value = ["/parole-cases"], produces = ["application/json"])
class ParoleCasesController(
  private val paroleCasesService: ParoleCasesService,
) {
  @GetMapping(value = ["/upcoming/{prisonCode}"])
  @ResponseStatus(code = HttpStatus.OK)
  @Operation(
    summary = "Get case details for cases with upcoming parole reviews",
    description = "Get case details for cases with upcoming parole reviews",
    security = [SecurityRequirement(name = "manage-pom-cases-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "A list of cases with upcoming parole reviews"),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized access to this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden access to this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  fun upcoming(@PathVariable(name = "prisonCode") prisonCode: String): List<ParoleCase> = paroleCasesService.upcomingAt(prisonCode)
}
