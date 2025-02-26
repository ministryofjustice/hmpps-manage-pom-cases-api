package uk.gov.justice.digital.hmpps.managepomcasesapi.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration(buildProperties: BuildProperties) {
  private val version: String = buildProperties.version

  @Value("\${info.app.name}")
  private lateinit var appName: String

  @Value("\${info.app.team.name}")
  private lateinit var teamName: String

  @Value("\${info.app.team.email}")
  private lateinit var teamEmail: String

  @Bean
  fun customOpenAPI(): OpenAPI = OpenAPI()
    .servers(
      listOf(
        Server().url("https://manage-pom-cases-api-dev.prison.service.justice.gov.uk").description("Development"),
        Server().url("http://localhost:8080").description("Local"),
      ),
    )
    .info(
      Info().title(appName).version(version)
        .contact(Contact().name(teamName).email(teamEmail)),
    )
    // TODO: Remove the default security schema and start adding your own schemas and roles to describe your
    // service authorisation requirements
    .components(
      Components().addSecuritySchemes(
        "manage-pom-cases-api-ui-role",
        SecurityScheme().addBearerJwtRequirement("ROLE_TEMPLATE_KOTLIN__UI"),
      ),
    )
    .addSecurityItem(SecurityRequirement().addList("manage-pom-cases-api-ui-role", listOf("read", "write")))
}

private fun SecurityScheme.addBearerJwtRequirement(role: String): SecurityScheme = type(SecurityScheme.Type.HTTP)
  .scheme("bearer")
  .bearerFormat("JWT")
  .`in`(SecurityScheme.In.HEADER)
  .name("Authorization")
  .description("A HMPPS Auth access token with the `$role` role.")
