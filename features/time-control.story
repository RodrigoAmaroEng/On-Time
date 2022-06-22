Meta: Time Control

Narrative:
To help the user on track the time spent on a task, he can select the task he is going to work on and later inform
that the work was finished/paused/stopped. The system automatically calculates the amount of time spent on the task

Scenario: Start first task
Given I have no current task
When I select a task
Then it will show as current task
And register the task start time

Scenario: Start another task
Given I have a current task
When I select another task
Then the new task will replace the previous one as the current task
And register the previous task end time
And register the new task start time

Scenario: Finish current task
Given I have a current task
When I press the task finish button
Then I will see no current task
And register the task end time