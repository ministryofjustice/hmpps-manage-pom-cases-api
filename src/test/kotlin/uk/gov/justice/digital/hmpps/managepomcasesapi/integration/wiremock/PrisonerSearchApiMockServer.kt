package uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class PrisonerSearchApiMockServer : WireMockServer(8092) {

  fun stubHealthPing(status: Int = 200) {
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

class PrisonerSearchApiExtension :
  BeforeAllCallback,
  AfterAllCallback,
  BeforeEachCallback {
  companion object {
    @JvmField
    val prisonerSearchApi = PrisonerSearchApiMockServer()
  }

  override fun beforeAll(context: ExtensionContext): Unit = prisonerSearchApi.start()
  override fun beforeEach(context: ExtensionContext): Unit = prisonerSearchApi.resetAll()
  override fun afterAll(context: ExtensionContext): Unit = prisonerSearchApi.stop()
}
