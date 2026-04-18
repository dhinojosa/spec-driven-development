package com.evolutionnext.features.account;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;

import static org.hamcrest.Matchers.equalTo;

class AccountAccessE2ETest {
    @Test
    void applicationHealthIsAvailableWhenE2EEnvironmentIsRunning() {
        Assumptions.assumeTrue(Boolean.parseBoolean(System.getenv().getOrDefault("RUN_E2E", "false")),
            "Set RUN_E2E=true after building the Jib image and starting docker-compose");
        Assumptions.assumeTrue(DockerClientFactory.instance().isDockerAvailable(),
            "Docker is required for e2e verification");

        RestAssured.given()
            .baseUri(System.getenv().getOrDefault("E2E_BASE_URI", "http://localhost:8080"))
            .when()
            .get("/health")
            .then()
            .statusCode(200)
            .body(equalTo("OK"));
    }
}
