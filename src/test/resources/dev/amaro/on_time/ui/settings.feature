Feature: First run
  After the app is initialized for the first time, the user needs to configure it providing the account to be
  used in order to communicate with Jira. Only then, he can see the list of tasks (if available) and interact with them.

  Background:
    Given the OnTime app environment

  Scenario: User starts the application for the first time
    Given I have never started the App
    When I start the App
    Then it will show message explaining it needs to be configured
    And show a button to start the configuration

  Scenario: User starts configuration
    Given I have never started the App
    When I start the App
    And I press the Start Configuration button
    Then it will show Settings Screen