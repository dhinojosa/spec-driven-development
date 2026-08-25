# Spec Driven Development

This project is for spec-driven-development.

It is a Maven multi-module project organized around a full application and
separate verification layers.

## Introduction Questions

1. Where do you work? What time zone are you in? 
2. What is your experience with AI?
   a. Who is your goto AI? Claude, ChatGPT, Copilot, Gemini.
3. What is your experience with Java?
4. What do you like to do for Zen? Cycling, Games, Sewing, Gardening, Hiking, Kids, ...

## Modules

- `full-application`
- `full-application-acceptance`
- `full-application-e2e`

## Toolchain

This project is currently being run with:

- Java: `26-zulu`
- Maven: `4.0.0-rc-5`
- Build command: `mvn validate`

Use SDKMAN to select the project toolchain from `.sdkmanrc`:

```bash
sdk env
mvn validate
```

Or select the candidates explicitly:

```bash
sdk use java 26-zulu
sdk use maven 4.0.0-rc-5
mvn validate
```

## Build Management

The parent POM centralizes dependency versions with Maven BOM imports for JUnit,
Testcontainers, Cucumber, Log4j, SLF4J, jqwik, AssertJ, Jackson, and REST Assured.

The parent POM also pins Maven plugin versions for resources, compiler, Surefire,
Failsafe, Surefire reports, and project info reports.

## Mutation Testing

PIT mutation testing is available for the production code and tests in
`full-application`. It is intentionally separate from the normal build.

Run it from the repository root:

```bash
mvn -pl full-application -Pmutation-testing test-compile
```

The HTML report is written to:

```text
full-application/target/pit-reports/index.html
```

## E2E Verification

Browser-level E2E tests live in the `full-application-e2e` module. Select that
module when you want Selenium browser verification to run:

```bash
mvn -pl full-application-e2e -am test
```

Root-level `mvn test` runs the whole reactor, including `full-application-e2e`.

## Run And Try The Application

Select Java 26 and Maven 4 as described above, and make sure Docker is running.

First, use Jib to build the complete application image in the local Docker
engine. Choose the command for your computer.

On an ARM64 computer, including an Apple Silicon Mac:

```bash
mvn -f full-application/pom.xml compile \
  com.google.cloud.tools:jib-maven-plugin:3.4.6:dockerBuild \
  -Djib.skip=false \
  -Djib.from.platforms=linux/arm64
```

On an AMD64 computer, including an Intel Mac or most Windows and Linux
computers:

```bash
mvn -f full-application/pom.xml compile \
  com.google.cloud.tools:jib-maven-plugin:3.4.6:dockerBuild \
  -Djib.skip=false \
  -Djib.from.platforms=linux/amd64
```

Start the application and PostgreSQL from the repository root:

```bash
docker compose -f full-application-e2e/docker-compose.yml \
  run --rm -p 8080:8080 full-application
```

Open `http://localhost:8080`. The database does not contain a ready-made
application account, so open the registration page and create this example
account:

```text
User name: casey
Password: correct-horse-battery-staple
```

After registering, try the account dashboard and Todo Today. Log out, then log
back in with the same user name and password.

## Stop The Application And Run E2E Tests

Stop the manually running application with `Control-C`, then stop its Compose
services:

```bash
docker compose -f full-application-e2e/docker-compose.yml down
```

Always stop the manually running Compose services before running the E2E tests.
The tests use Testcontainers to start and stop the application and PostgreSQL
from the same Compose file.

Run the E2E tests from the repository root:

```bash
mvn -pl full-application-e2e -am test
```

## Run From Maven Instead

To run the application directly from Maven, first start the local PostgreSQL
database:

```bash
cd full-application
docker compose up -d
```

From the repository root, run:

```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/orders \
DATABASE_USERNAME=postgres \
DATABASE_PASSWORD=postgres \
PORT=8080 \
mvn -pl full-application exec:java
```

To run the application from IntelliJ, use:

- main class: `com.evolutionnext.Runner`
- environment variable: `DATABASE_URL=jdbc:postgresql://localhost:5432/orders`
- environment variable: `DATABASE_USERNAME=postgres`
- environment variable: `DATABASE_PASSWORD=postgres`
- environment variable: `PORT=8080`

If `DATABASE_URL` is unset, the application uses the in-memory account
repository.

## License

This project is licensed under the MIT License. See `LICENSE`.
