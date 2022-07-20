Feature: Task listing interface - Main Screen
  After the app is initialized for the first time, the user needs to configure it providing the account to be
  used in order to communicate with Jira. Only then, he can see the list of tasks (if available) and interact with them.

  Background:
    Given the OnTime app environment


  Scenario: User starts the application for the first time
    Given I have never started the App
    When I start the App
    Then it will show message explaining it needs to be configured
    And show a button to start the configuration

  Scenario: User starts the application after having it configured
    Given I already configured the application
    And there are available tasks to work
    When I start the App
    Then it will display the list of tasks available
    And the filter options will be visible

  Scenario: User has no tasks for work
    Given I already configured the application
    And there are no available tasks to work
    When I start the App
    Then it will present a message telling there are no tasks available

  Scenario: Internet connection issues
    Given I already configured the application
    And there is no internet connection
    When I start the App
    Then it will present a message informing it wasn't able to retrieve the tasks

  Scenario: User filter only his tasks
    Given I already configured the application
    And there are available tasks to work
    And the 'Only Assigned To Me' option is not activated
    When I start the App
    And I press the Filter Assigned to Me button
    Then it will show only my task

  Scenario: User filter only his tasks but has none
    Given I already configured the application
    And none of the available tasks are assigned to me
    And the 'Only Assigned To Me' option is not activated
    When I start the App
    And I press the Filter Assigned to Me button
    Then it will present a message telling there are no tasks available

  Scenario: User removes the assignation filter
    Given I already configured the application
    And there are available tasks to work
    And the 'Only Assigned To Me' option is activated
    When I start the App
    And I press the Filter Assigned to Me button
    Then it will show all tasks
