package uk.gov.justice.digital.hmpps.managepomcasesapi.poms

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
import uk.gov.justice.digital.hmpps.managepomcasesapi.service.StaffService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestController
@PreAuthorize("hasRole('ROLE_MANAGE_POM_CASES__MANAGE_POM_CASES_UI')")
@RequestMapping(value = ["/poms"], produces = ["application/json"])
class PomController(
  private val staffService: StaffService,
) {
  @GetMapping(value = ["/{staffId}/is-pom/{prisonCode}"])
  @ResponseStatus(code = HttpStatus.OK)
  @Operation(
    summary = "Check if a staff member is a POM at a prison",
    description = "Check if a staff member is a POM at a prison",
    security = [SecurityRequirement(name = "manage-pom-cases-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "Whether the staff member is a POM at the prison"),
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
  fun hasPomAtPrison(
    @PathVariable(name = "staffId") staffId: Int,
    @PathVariable(name = "prisonCode") prisonCode: String,
  ) = staffService.hasPomRole(staffId, prisonCode)
}
