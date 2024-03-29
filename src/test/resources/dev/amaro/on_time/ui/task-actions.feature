Feature: Task Actions
  Tasks provide a number of actions that can be performed by the user to inform his intentions to work on or to
  update the status of his work. Currently, the user can self-assign a task or change its current state

  Background:
    Given the OnTime app environment

#  Scenario: Assign action is available
#    Given a task that is Unassigned
#    And the 'Only Assigned To Me' option is not activated
#    When this task is rendered
#    And I move the cursor over the task options
#    Then it will show the assign button
#    And the task will have an icon showing it's NOT assigned

#  Scenario: Assign action is unavailable
#    Given a task that is assigned to the user
#    When this task is rendered
#    Then it will NOT show the assign button
#    And the task will have an icon showing it's assigned
#
#  Scenario: Assign a task to the user
#    Given a task that is Unassigned
#    When this task is rendered
#    And I press the assign button
#    Then it will perform assignation
#    And the task will have show the icon informing task is assigned
#
#  Scenario: Move task to Working status is available
#    Given a task which status is Not Started OR To Do
#    When this task is rendered
#    Then it will show the Set as Working button
#
#  Scenario: Move task to Done status is available
#    Given a task which status is Working OR Review
#    When this task is rendered
#    Then it will show the Set as Done button
#
#  Scenario: Move task to Ready for QA is available
#    Given a task which status is Review OR Working
#    When this task is rendered
#    Then it will show the Set as Ready for QA button
#
#  Scenario: Move task to Review is available
#    Given a task which status is Working
#    When this task is rendered
#    Then it will show the Set as Review button