package com.evolutionnext.features.welcome;

import com.evolutionnext.AccountApplication;
import com.evolutionnext.features.account.infrastructure.adapter.out.InMemoryAccountRepository;
import com.google.inject.Inject;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public final class WelcomePageSteps {
    private final WelcomeScenarioState state;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Inject
    public WelcomePageSteps(WelcomeScenarioState state) {
        this.state = state;
    }

    @Before
    public void startApplication() {
        state.rememberServer(new AccountApplication().start(0, new InMemoryAccountRepository()));
    }

    @After
    public void stopApplication() {
        state.stopServer();
    }

    @Given("I am an anonymous user")
    public void iAmAnAnonymousUser() {
        assertThat(state.baseUri()).isNotNull();
    }

    @When("I navigate to the home page")
    public void iNavigateToTheHomePage() throws Exception {
        state.rememberLastResponse(get("/"));
    }

    @Then("I should see a welcome page")
    public void iShouldSeeAWelcomePage() {
        assertThat(state.lastResponse().statusCode()).isEqualTo(200);
        assertThat(state.lastResponse().body()).contains("One focused session at a time.");
    }

    @And("I should see a picture of a tomato")
    public void iShouldSeeAPictureOfATomato() {
        assertThat(state.lastResponse().body()).contains("/assets/welcome-tomato.png");
    }

    @And("I should see a Pomodoro Time image")
    public void iShouldSeeAPomodoroTimeImage() {
        assertThat(state.lastResponse().body()).contains("/assets/welcome-pomodoro-technique.jpg");
    }

    private HttpResponse<String> get(String path) throws Exception {
        return httpClient.send(HttpRequest.newBuilder(state.baseUri().resolve(path)).GET().build(),
            HttpResponse.BodyHandlers.ofString());
    }
}
