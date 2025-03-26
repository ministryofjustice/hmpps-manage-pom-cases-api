package uk.gov.justice.digital.hmpps.managepomcasesapi.integration.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
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

  fun stubFindByPrisonIdResponses(vararg pageResponse: String, prisonCode: String? = "LEI") {
    pageResponse.forEachIndexed { i, response ->
      stubFor(
        get("/prisoner-search-api/prisoner-search/prison/$prisonCode?page=$i&size=1500&include-restricted-patients=true").willReturn(
          aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(response)
            .withStatus(200),
        ),
      )
    }
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
