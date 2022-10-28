Feature: First run
  After the app is initialized for the first time, the user needs to configure it providing the account to be
  used in order to communicate with Jira. Only then, he can see the list of tasks (if available) and interact with them.

  Background:
    Given the OnTime app environment
    And I have never started the App

  Scenario: User starts the application for the first time
    When I start the App
    Then it will show message explaining it needs to be configured
    And show a button to start the configuration

  Scenario: User starts configuration
    When I start the App
    And I press the Start Configuration button
    Then it will show Settings Screen

  Scenario: User fills the configuration and save
    Given there are available tasks to work
    When I start the App
    And I press the Start Configuration button
    And I fill all settings
    And I press the save button
    Then it will show the Main Screen
    And it will display the list of tasks available

  Scenario: User tries to save without informing all settings
    When I start the App
    And I press the Start Configuration button
    And I press the save button
    Then it will present an error message informing that the user should provide all settings