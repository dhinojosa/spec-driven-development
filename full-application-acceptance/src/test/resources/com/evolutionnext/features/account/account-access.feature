@ACC-0001
Feature: Account access

    Anonymous users need to register accounts and registered users need to log in
    before using the dashboard page.

    @ACC-0001-02
    Scenario: Anonymous user registers an account with a user name and password
        Given an anonymous user is on the account registration page
        When they register with user name "casey" and password "correct-horse-battery-staple"
        Then an account exists for user name "casey"
        And the user is shown the dashboard page

    @ACC-0001-03
    Scenario: Registered user logs in and is taken to the dashboard page
        Given an account exists for user name "casey" and password "correct-horse-battery-staple"
        And the user is on the login page
        When they log in with user name "casey" and password "correct-horse-battery-staple"
        Then the user is taken to the dashboard page

    @ACC-0001-04
    Scenario: Anonymous user registers with a password shorter than 8 characters
        Given an anonymous user is on the account registration page
        When they register with user name "casey" and password "short"
        Then the user remains on the account registration page
        And the registration page shows a password must be at least 8 characters message
        And the registration page still shows the entered user name
        And the password field is empty

    Scenario: User logs in with bad credentials and sees an invalid credentials message
        Given an account exists for user name "casey" and password "correct-horse-battery-staple"
        And the user is on the login page
        When they log in with user name "casey" and password "wrong-password"
        Then the user remains on the login page
        And the page shows an invalid credentials message

    @ACC-0001-05
    Scenario Outline: Authenticated user can log out from any secure page
        Given an account exists for user name "casey" and password "correct-horse-battery-staple"
        And the user is logged in
        And the user is on the <page> page
        When the user clicks the log out button
        Then the user is returned to the home page
        And the home page does not show secure-area navigation

        Examples:
            | page               |
            | dashboard          |
            | todo today         |
            | activity inventory |
            | record sheet       |
