@ACC-0001
Feature: Account access

  Anonymous users need to register accounts and registered users need to log in
  before using the pomodoro page.

  Scenario: Anonymous user registers an account with a user name and password
    Given an anonymous user is on the account registration page
    When they register with user name "casey" and password "correct-horse-battery-staple"
    Then an account exists for user name "casey"
    And the user is shown the login page

  Scenario: Registered user logs in and is taken to the pomodoro page
    Given an account exists for user name "casey" and password "correct-horse-battery-staple"
    And the user is on the login page
    When they log in with user name "casey" and password "correct-horse-battery-staple"
    Then the user is taken to the pomodoro page

  Scenario: User logs in with bad credentials and sees an invalid credentials message
    Given an account exists for user name "casey" and password "correct-horse-battery-staple"
    And the user is on the login page
    When they log in with user name "casey" and password "wrong-password"
    Then the user remains on the login page
    And the page shows an invalid credentials message
