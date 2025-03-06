package uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class PrisonApiMockServer : WireMockServer(8092) {

  // NOTE: I do not know why in the stub methods we have to prefix the url with the prison-X-api but in the others we dont

  fun stubHealthPing(status: Int = 200) {
    stubFor(
      get("/prison-api/health/ping").willReturn(
        aResponse()
          .withHeader("Content-Type", "application/json")
          .withBody(if (status == 200) """{"status":"UP"}""" else """{"status":"DOWN"}""")
          .withStatus(status),
      ),
    )
  }

  fun stubPrisonerSearchHealthPing(status: Int = 200) {
    stubFor(
      get("/prisoner-search-api/health/ping").willReturn(
        aResponse()
          .withHeader("Content-Type", "application/json")
          .withBody(if (status == 200) """{"status":"UP"}""" else """{"status":"DOWN"}""")
          .withStatus(status),
      ),
    )
  }

  fun stubUncheckedPrisonerSearchResponse() {
    stubFor(
      get(urlPathMatching("/prisoner-search/prison/[A-Z]{3}"))
        .willReturn(
          aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody("""{ "content": [] }"""),
        ),
    )
  }
}

class PrisonApiExtension :
  BeforeAllCallback,
  AfterAllCallback,
  BeforeEachCallback {
  companion object {
    @JvmField
    val prisonApi = PrisonApiMockServer()
  }

  override fun beforeAll(context: ExtensionContext): Unit = prisonApi.start()
  override fun beforeEach(context: ExtensionContext): Unit = prisonApi.resetAll()
  override fun afterAll(context: ExtensionContext): Unit = prisonApi.stop()
}
