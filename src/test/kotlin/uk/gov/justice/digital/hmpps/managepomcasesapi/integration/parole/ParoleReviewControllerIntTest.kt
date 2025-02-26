package uk.gov.justice.digital.hmpps.managepomcasesapi.integration.parole

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.managepomcasesapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.managepomcasesapi.parole.ParoleReview

class ParoleReviewControllerIntTest : IntegrationTestBase() {

  @Nested
  @DisplayName("GET /parole-reviews")
  inner class GetParoleReviewsEndpoint {
    @Test
    fun `should return unauthorized if no token`() {
      webTestClient.get()
        .uri("/parole-reviews")
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should return forbidden if no role`() {
      webTestClient.get()
        .uri("/parole-reviews")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return forbidden if wrong role`() {
      webTestClient.get()
        .uri("/parole-reviews")
        .headers(setAuthorisation(roles = listOf("ROLE_WRONG")))
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Sql(
      "classpath:test_data/reset.sql",
      "classpath:test_data/parole/some_parole_reviews.sql",
    )
    @Test
    fun `should return OK`() {
      val reviews = webTestClient.get()
        .uri("/parole-reviews")
        .headers(setAuthorisation(roles = listOf("ROLE_TEMPLATE_KOTLIN__UI")))
        .exchange()
        .expectStatus().isOk
        .expectBody(object : ParameterizedTypeReference<List<ParoleReview>>() {})
        .returnResult().responseBody!!

      assertThat(reviews).hasSize(2)
    }
  }
}
