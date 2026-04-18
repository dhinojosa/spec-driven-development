# TECH-0001 Local Dev Database

Governed by: Technical task

Business spec: Not applicable

Reason no Cucumber spec applies:

This task supports local developer workflow for running and debugging the
application manually. It does not introduce business behavior.

## Build / Tooling

- [x] Add `full-application/docker-compose.yml` for local Postgres.
- [x] Add `full-application/init.sql` for local account schema setup.
- [x] Add Maven `exec-maven-plugin` configuration for running `com.evolutionnext.Runner`.
- [x] Configure local run documentation with `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, and `PORT`.

## Application Runtime

- [x] Verify `com.evolutionnext.Runner` can connect to the local Postgres container.
- [x] Verify the application can still run with the in-memory repository when `DATABASE_URL` is unset.
- [x] Verify `/health` returns `OK` when running locally.

## Documentation

- [x] Document local database startup with `docker compose up -d` from `full-application`.
- [x] Document local application startup from IntelliJ.
- [x] Document local application startup from Maven.
- [x] Document manual URLs for `/health`, `/account/register`, and `/account/login`.

## Validation

- [x] Run `mvn verify`.
- [x] Start local Postgres with Docker Compose.
- [x] Start `com.evolutionnext.Runner` against local Postgres.
- [x] Verify `/health` returns `OK`.
- [x] Verify the registration and login pages load locally.
