# TECH-0009 Full Application Mutation Testing

Governed by: Technical task `TECH-0009`

Cucumber specification: Not applicable. This task adds mutation-test tooling
and does not change application behavior.

## Scope

- [x] Run PIT against production code in the `full-application` module only.
- [x] Keep `full-application-acceptance` and `full-application-e2e` out of the
  PIT configuration because they do not contain production code to mutate.
- [x] Keep the existing acceptance, REST Assured, Selenium Page Object, and
  architecture tests unchanged.

## Maven Configuration

- [x] Add managed PIT Maven plugin and JUnit Platform plugin versions to the
  root POM.
- [x] Add the PIT Maven plugin configuration to
  `full-application/pom.xml` only.
- [x] Put mutation testing behind a `mutation-testing` Maven profile so the
  normal `mvn verify` command does not run PIT.
- [x] Configure PIT to mutate production classes under
  `com.evolutionnext`.
- [x] Configure PIT to use the tests in `full-application`.
- [x] Write the mutation report under
  `full-application/target/pit-reports`.

## Initial Mutation Run

- [x] Run PIT in dry-run mode first and confirm it can discover the compiled
  production classes and tests with Java 26, JUnit, and jqwik.
- [x] Run the full mutation analysis for `full-application`.
- [x] Record the initial mutation score and any classes that PIT could not
  analyze.
- [x] Do not add an arbitrary minimum mutation score during this task; use the
  first successful report as the baseline for deciding a useful minimum later.
- [x] Document the exact Maven command for running mutation testing from the
  repository root.

## Unchanged Areas

- [x] Keep application source code and business behavior unchanged.
- [x] Keep Cucumber specifications and step definitions unchanged.
- [x] Keep Jib, Docker Compose, Testcontainers, REST Assured, and Selenium
  configuration unchanged.
- [x] Record that new API, browser, property-based, and architecture tests are
  not required because this task only adds a new way to evaluate the existing
  tests.

## Validation

- [x] Run the PIT dry run for `full-application`.
- [x] Run the full PIT mutation report for `full-application`.
- [x] Confirm the HTML report exists under
  `full-application/target/pit-reports`.
- [x] Confirm the report contains mutations from the expected
  `full-application` production packages.
- [x] Run `mvn verify` and confirm the normal build still passes without
  automatically running PIT.

## Validation Notes

- PIT Maven `1.25.8` and PIT JUnit Platform plugin `1.2.3` ran on Java 26
  with JUnit Platform `6.0.3` and jqwik tests.
- The dry run found 88 tests and generated 251 mutations without running tests
  against them.
- The full mutation run completed in 3 minutes 35 seconds. It reported 87% line
  coverage, 81% mutation coverage, and 92% test strength.
- Of 251 mutations, 168 were killed by a failing test, 36 timed out and were
  treated as detected, 17 survived, and 30 had no test coverage.
- PIT analyzed every selected class; no class-analysis failures were reported.
- The mutation report is
  `full-application/target/pit-reports/index.html`.
- Run mutation testing from the repository root with:

  ```bash
  mvn -pl full-application -Pmutation-testing test-compile
  ```

- The final `mvn verify` completed successfully across all four reactor
  projects. The normal build did not run PIT.
- Maven 4 initially encountered its known local-repository lock problem. Maven
  3.9.16 downloaded the PIT Maven plugin but could not compile this project
  because the Maven Compiler Plugin requires Maven 4. After the stale lock
  disappeared, all PIT and final verification runs completed with Maven
  `4.0.0-rc-5`.
