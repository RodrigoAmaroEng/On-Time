Feature: Time Control
  To help the user on track the time spent on a task, he can select the task he is going to work on and later inform
  that the work was finished/paused/stopped. The system automatically calculates the amount of time spent on the task

  Background:
    Given the OnTime app environment
    And I already configured the application
    And there are available tasks to work

  Scenario: Start the app with a working task
    Given I have a current task
    When I start the App
    Then it will show as current task

  Scenario: Start first task
    Given I have no current task
    When I start the App
    And I select a task
    Then it will show as current task
    And register the task start time

  Scenario: Start another task
    Given I have a current task
    And the 'Only Assigned To Me' option is not activated
    When I start the App
    And I select another task
    Then the new task will be shown as current task
    And register the previous task end time
    And register the new task start time

  Scenario: Finish current task
    Given I have a current task
    When I start the App
    And I press the task finish button
    Then I will see no current task
    And register the task end time

  Scenario: Start pomodoro for current task
    Given I have a current task
    When I start the App
    And I press the pomodoro start button
    Then I will see the pomodoro timer

  Scenario: Pomodoro finishes after 25 minutes and start a Small Break timer
    Given I have a current task
    When I start the App
    And I press the pomodoro start button
    And I wait 25 minutes
    Then I will see the Small Break timer
    And I will see no current task

  Scenario: Small Break timer finishes after 5 minutes
    Given I have a current task
    When I start the App
    And I press the pomodoro start button
    And I wait 25 minutes
    And I wait 5 minutes
    Then there will be no Small Break timer

  Scenario: Resume the work on last task
    Given I have no current task
    And I have previously worked on task ABC-1234
    When I start the App
    And I press the Resume Task button
    Then it will show the task ABC-1234 as the current one
    And the Resume Task button show not be visible