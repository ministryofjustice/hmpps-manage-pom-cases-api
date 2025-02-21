# hmpps-manage-pom-cases-api

[![repo standards badge](https://img.shields.io/badge/endpoint.svg?&style=flat&logo=github&url=https%3A%2F%2Foperations-engineering-reports.cloud-platform.service.justice.gov.uk%2Fapi%2Fv1%2Fcompliant_public_repositories%2Fhmpps-manage-pom-cases-api)](https://operations-engineering-reports.cloud-platform.service.justice.gov.uk/public-report/hmpps-manage-pom-cases-api "Link to report")
[![Docker Repository on ghcr](https://img.shields.io/badge/ghcr.io-repository-2496ED.svg?logo=docker)](https://ghcr.io/ministryofjustice/hmpps-manage-pom-cases-api)
[![API docs](https://img.shields.io/badge/API_docs_-view-85EA2D.svg?logo=swagger)](https://hmpps-manage-pom-cases-api-dev.prison.service.justice.gov.uk/webjars/swagger-ui/index.html?configUrl=/v3/api-docs)

New MPC written in Kotlin.
Digital service for allocating, managing and handing over POM cases. API repository.  

## Running the application locally

The application comes with a `dev` spring profile that includes default settings for running locally. This is not
necessary when deploying to kubernetes as these values are included in the helm configuration templates -
e.g. `values-dev.yaml`.

There is also a `docker-compose.yml` that can be used to run a local instance of the application in docker and also an
instance of HMPPS Auth (required if your service calls out to other services using a token).

```bash
docker compose pull && docker compose up
```

will build the application and run it and HMPPS Auth within a local docker instance.

## Running the application in IntelliJ

```bash
docker compose pull && docker compose up --scale hmpps-manage-pom-cases-api=0
```

will just start a docker instance of HMPPS Auth. The application should then be started with a `dev` active profile
in Intellij.

## Checking changes before pushing

```
./gradlew check
```

## Linting the code

```
./gradlew ktlintCheck
./gradlew ktlintFormat
```
