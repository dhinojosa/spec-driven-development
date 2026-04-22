package com.evolutionnext.features.account;

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

public final class AccountAccessSteps {
    private final AccountScenarioState state;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Inject
    public AccountAccessSteps(AccountScenarioState state) {
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

    @Given("an anonymous user is on the account registration page")
    public void anonymousUserIsOnRegistrationPage() throws Exception {
        state.rememberLastResponse(get("/account/register"));
        assertThat(state.lastResponse().body()).contains("Register account");
    }

    @When("they register with user name {string} and password {string}")
    public void registerWithUserNameAndPassword(String userName, String password) throws Exception {
        state.rememberLastUserName(userName);
        state.rememberLastResponse(post("/account/register", "userName=%s&password=%s".formatted(userName, password)));
    }

    @Then("an account exists for user name {string}")
    public void accountExistsForUserName(String userName) throws Exception {
        var response = get("/account?userName=" + userName);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains(userName);
    }

    @And("the user is shown the dashboard page")
    public void userIsShownDashboardPage() {
        assertThat(state.lastResponse().body()).contains("Dashboard");
        assertThat(state.lastResponse().body()).contains("/todo-today");
        assertThat(state.lastResponse().body()).contains("/activity-inventory");
        assertThat(state.lastResponse().body()).contains("/record-sheet");
    }

    @Given("an account exists for user name {string} and password {string}")
    public void accountExistsForUserNameAndPassword(String userName, String password) throws Exception {
        registerWithUserNameAndPassword(userName, password);
        accountExistsForUserName(userName);
    }

    @And("the user is on the login page")
    public void userIsOnLoginPage() throws Exception {
        state.rememberLastResponse(get("/account/login"));
        assertThat(state.lastResponse().body()).contains("Login");
    }

    @When("they log in with user name {string} and password {string}")
    public void logInWithUserNameAndPassword(String userName, String password) throws Exception {
        state.rememberLastResponse(post("/account/login", "userName=%s&password=%s".formatted(userName, password)));
    }

    @Then("the user is taken to the dashboard page")
    public void userIsTakenToDashboardPage() {
        assertThat(state.lastResponse().statusCode()).isEqualTo(200);
        assertThat(state.lastResponse().body()).contains("Dashboard");
        assertThat(state.lastResponse().body()).contains("/todo-today");
        assertThat(state.lastResponse().body()).contains("/activity-inventory");
        assertThat(state.lastResponse().body()).contains("/record-sheet");
    }

    @Then("the user remains on the login page")
    public void userRemainsOnLoginPage() {
        assertThat(state.lastResponse().statusCode()).isEqualTo(401);
        assertThat(state.lastResponse().body()).contains("Login");
    }

    @Then("the user remains on the account registration page")
    public void userRemainsOnAccountRegistrationPage() {
        assertThat(state.lastResponse().statusCode()).isEqualTo(422);
        assertThat(state.lastResponse().body()).contains("Register account");
    }

    @And("the page shows an invalid credentials message")
    public void pageShowsInvalidCredentialsMessage() {
        assertThat(state.lastResponse().body()).contains("Invalid user name or password");
    }

    @And("the registration page shows a password must be at least 8 characters message")
    public void registrationPageShowsPasswordValidationMessage() {
        assertThat(state.lastResponse().body()).contains("Password must be at least 8 characters");
    }

    @And("the registration page still shows the entered user name")
    public void registrationPageStillShowsEnteredUserName() {
        assertThat(state.lastResponse().body()).contains("name=\"userName\" autocomplete=\"username\" value=\"casey\"");
    }

    @And("the password field is empty")
    public void passwordFieldIsEmpty() {
        assertThat(state.lastResponse().body())
            .contains("name=\"password\" type=\"password\" autocomplete=\"new-password\" value=\"\"");
    }

    private HttpResponse<String> get(String path) throws Exception {
        return httpClient.send(HttpRequest.newBuilder(state.baseUri().resolve(path)).GET().build(),
            HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String path, String body) throws Exception {
        return httpClient.send(HttpRequest.newBuilder(state.baseUri().resolve(path))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build(),
            HttpResponse.BodyHandlers.ofString());
    }
}
