package com.evolutionnext.features.todotoday;

import com.evolutionnext.AccountApplication;
import com.evolutionnext.features.account.infrastructure.adapter.out.InMemoryAccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodoTodaySeleniumE2ETest {
    private com.sun.net.httpserver.HttpServer server;
    private URI baseUri;
    private WebDriver browser;

    @BeforeEach
    void startApplication() {
        Assumptions.assumeTrue(Boolean.parseBoolean(System.getenv().getOrDefault("RUN_UI_E2E", "false")),
            "Set RUN_UI_E2E=true to run Selenium browser verification");
        server = new AccountApplication().start(0, new InMemoryAccountRepository());
        baseUri = URI.create("http://localhost:" + server.getAddress().getPort());
        browser = new SafariDriver();
    }

    @AfterEach
    void stopApplication() {
        if (browser != null) {
            browser.quit();
        }
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void browserFlowCanAddEstimateWarnAndCompleteATask() {
        browser.get(baseUri.resolve("/account/register").toString());
        browser.findElement(By.name("userName")).sendKeys("casey");
        browser.findElement(By.name("password")).sendKeys("correct-horse-battery-staple");
        browser.findElement(By.cssSelector("button[type='submit']")).click();

        waitForText("Dashboard");
        browser.findElement(By.linkText("Todo today page")).click();
        waitForText("Todo today page");

        browser.findElement(By.name("taskName")).sendKeys("Build conference workshop");
        browser.findElement(By.cssSelector("form[action='/todo-today/task'] button")).click();
        waitForText("Build conference workshop");

        var estimateForms = browser.findElements(By.cssSelector("form[action='/todo-today/task/estimate']"));
        estimateForms.get(0).findElement(By.name("estimatedPomodoros")).clear();
        estimateForms.get(0).findElement(By.name("estimatedPomodoros")).sendKeys("7");
        estimateForms.get(0).findElement(By.tagName("button")).click();
        waitForText("Large task warning");

        var completedForms = browser.findElements(By.cssSelector("form[action='/todo-today/task/completed-pomodoros']"));
        completedForms.get(0).findElement(By.name("completedPomodoros")).clear();
        completedForms.get(0).findElement(By.name("completedPomodoros")).sendKeys("8");
        completedForms.get(0).findElement(By.tagName("button")).click();
        waitForInputValue("form[action='/todo-today/task/completed-pomodoros'] input[name='completedPomodoros']", "8");

        browser.findElement(By.cssSelector("form[action='/todo-today/task/complete'] button")).click();
        waitForText("Completed over the estimate");

        var taskCard = browser.findElement(By.cssSelector(".todo-task"));
        assertThat(taskCard.getAttribute("class")).contains("todo-task--complete");
        assertThat(taskCard.getText()).contains("Build conference workshop");
        assertThat(taskCard.getText()).contains("Large task warning");
        assertThat(taskCard.getText()).contains("Completed over the estimate");
        assertThat(textDecorations(taskCard)).anyMatch(value -> value.contains("line-through"));
    }

    private void waitForText(String text) {
        new WebDriverWait(browser, Duration.ofSeconds(5))
            .until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), text));
    }

    private void waitForInputValue(String cssSelector, String value) {
        new WebDriverWait(browser, Duration.ofSeconds(5))
            .until(driver -> value.equals(driver.findElement(By.cssSelector(cssSelector)).getDomProperty("value")));
    }

    private static List<String> textDecorations(WebElement element) {
        return List.of(
            element.getCssValue("text-decoration"),
            element.getCssValue("text-decoration-line"));
    }
}
