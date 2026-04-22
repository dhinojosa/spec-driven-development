Feature: Welcome Page
    As an anonymous user
    I want to see a welcome page when I visit the website
    So that I know I'm on the Pomodoro Time application

    @ACC-0002
    Scenario: Anonymous user visits the website
        Given I am an anonymous user
        When I navigate to the home page
        Then I should see a welcome page
        And I should see a picture of a tomato
        And I should see the text "Pomodoro Time!"
