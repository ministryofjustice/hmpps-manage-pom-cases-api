package uk.gov.justice.digital.hmpps.managepomcasesapi.parole

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestController
@PreAuthorize("hasRole('ROLE_TEMPLATE_KOTLIN__UI')")
@RequestMapping(value = ["/parole-reviews"], produces = ["application/json"])
class ParoleReviewController(
  private val service: ParoleReviewService,
) {
  @GetMapping(value = [""])
  @ResponseStatus(code = HttpStatus.OK)
  @Operation(
    summary = "Get all parole reviews",
    description = "Get all parole reviews",
    security = [SecurityRequirement(name = "manage-pom-cases-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "A list of local delivery unit mailboxes"),
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
  fun getParoleReviews(): List<ParoleReview> = service.getParoleReviews()
}
