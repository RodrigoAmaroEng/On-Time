
package dev.amaro.on_time.ui.steps

import androidx.compose.ui.test.*
import assertk.assertThat
import assertk.assertions.isEqualTo
import dev.amaro.on_time.Samples
import dev.amaro.on_time.log.LogEvent
import dev.amaro.on_time.log.StoreItemTask
import dev.amaro.on_time.ui.RunCucumberTest
import dev.amaro.on_time.ui.Tags
import dev.amaro.on_time.ui.TextResources
import io.cucumber.java.en.Then
import io.mockk.CapturingSlot
import io.mockk.slot
import io.mockk.verify


@OptIn(ExperimentalTestApi::class)
class AssertionSteps : Step {

    @Then("it will show message explaining it needs to be configured")
    fun step1() = onScenarioContext {
        onNodeWithText(TextResources.Errors.NoConfiguration).assertExists()
    }

    @Then("show a button to start the configuration")
    fun step2() = onScenarioContext {
        onNodeWithText(TextResources.Actions.StartConfiguration).assertExists()
    }

    @Then("it will display the list of tasks available")
    fun step3() = onScenarioContext {
        onNodeWithTag(Tags.QueryResults).assertExists()
    }

    @Then("the filter options will be visible")
    fun step4() = onScenarioContext {
        onNodeWithTag(Tags.TaskFilters).assertExists()
    }

    @Then("it will present a message telling there are no tasks available")
    fun step5() = onScenarioContext {
        onNodeWithText(TextResources.Errors.NoTasksAvailable).assertExists()
    }

    @Then("it will present a message informing it wasn't able to retrieve the tasks")
    fun step6() = onScenarioContext {
        onNodeWithText(TextResources.Errors.NetworkError).assertExists()
    }

    @Then("it will show only my task")
    fun step7() = onScenarioContext {
        onNodeWithTag(Tags.QueryResults).apply {
            assertExists()
            onChildren().filterToOne(hasAnyDescendant(hasText(Samples.TASK_ID_1))).assertExists()
        }

        onNodeWithText(Samples.TASK_ID_2).assertDoesNotExist()
        onNodeWithText(Samples.TASK_ID_3).assertDoesNotExist()
    }

    @Then("it will show all tasks")
    fun step8() = onScenarioContext {
        onNodeWithTag(Tags.QueryResults).apply {
            assertExists()
            onChildren().filterToOne(hasAnyDescendant(hasText(Samples.TASK_ID_1)) ).assertExists()
            onChildren().filterToOne(hasAnyDescendant(hasText(Samples.TASK_ID_2))).assertExists()
            onChildren().filterToOne(hasAnyDescendant(hasText(Samples.TASK_ID_3))).assertExists()
        }
    }

    @Then("it will show as current task")
    fun step9() = onScenarioContext {
        onNodeWithTag(Tags.CurrentTask).apply {
            assert(hasAnyDescendant(hasText(Samples.TASK_ID_1)))
        }
    }

    @Then("register the task start time")
    fun step10() = onScenarioContext {
        val slot: CapturingSlot<StoreItemTask> = slot()
        verify { RunCucumberTest.storage.include(capture(slot)) }
        assertThat(slot.captured.event).isEqualTo(LogEvent.TASK_START)
        assertThat(slot.captured.task).isEqualTo(Samples.task1)
    }

    @Then("the new task will be shown as current task")
    fun step11() = onScenarioContext {
        onNodeWithTag(Tags.CurrentTask).apply {
            assert(hasAnyDescendant(hasText(Samples.TASK_ID_2)))
        }
    }

    @Then("register the previous task end time")
    fun step12() = onScenarioContext {
        val slots = mutableListOf<StoreItemTask>()
        verify { RunCucumberTest.storage.include(capture(slots)) }
        assertThat(slots[0].event).isEqualTo(LogEvent.TASK_END)
        assertThat(slots[0].task).isEqualTo(Samples.task1)
    }

    @Then("register the new task start time")
    fun step13() = onScenarioContext {
        val slots = mutableListOf<StoreItemTask>()
        verify { RunCucumberTest.storage.include(capture(slots)) }
        assertThat(slots[1].event).isEqualTo(LogEvent.TASK_START)
        assertThat(slots[1].task).isEqualTo(Samples.task2)
    }

    @Then("I will see no current task")
    fun step14() = onScenarioContext {
        onNodeWithTag(Tags.CurrentTask).assertDoesNotExist()
    }

    @Then("register the task end time")
    fun step15() = onScenarioContext {
        val slots = mutableListOf<StoreItemTask>()
        verify { RunCucumberTest.storage.include(capture(slots)) }
        assertThat(slots[0].event).isEqualTo(LogEvent.TASK_END)
        assertThat(slots[0].task).isEqualTo(Samples.task1)
    }

    @Then("it will show the assign button")
    fun step16() = onScenarioContext {
        onNodeWithTag("Task-CST-123").apply {
            assert(hasAnyDescendant(hasTestTag("ASSIGN-BUTTON")))
        }
    }

    @Then("the task will have an icon showing it's NOT assigned")
    fun step17() = onScenarioContext {
        onNodeWithTag("Task-CST-123").apply {
            assert(hasAnyDescendant(hasTestTag("NOT-ASSIGNED-ICON")))
        }
    }

    @Then("it will show Settings Screen")
    fun step18() = onScenarioContext {
        onNodeWithTag(Tags.SettingsScreen).assertExists()
    }
    @Then("it will show the Main Screen")
    fun step19() = onScenarioContext {
        onNodeWithTag(Tags.MainScreen).assertExists()
    }

    @Then("it will present an error message informing that the user should provide all settings")
    fun step20() = onScenarioContext {
        onNodeWithText(TextResources.Errors.NotAllSettingsWereInformed).assertExists()
    }

    @Then("it will show the Projects setting on the Settings screen")
    fun step21() = onScenarioContext {
        onNodeWithTag(Tags.Settings_Prop_Projects).assertExists()
    }

    @Then("the Projects settings will show \"CAT,CST\"")
    fun step22() = onScenarioContext {
        onNodeWithTag(Tags.Settings_Prop_Projects + "_Value").assertTextContains("CAT,CST")
    }

    @Then("I will see the pomodoro timer")
    fun step23() = onScenarioContext {
        onNodeWithTag(Tags.PomodoroTimer).assertExists()
    }

    @Then("I will see the Small Break timer")
    fun step24() = onScenarioContext {
        onNodeWithTag(Tags.BreakTimer).assertExists()
    }

    @Then("there will be no Small Break timer")
    fun step25() = onScenarioContext {
        onNodeWithTag(Tags.BreakTimer).assertDoesNotExist()
    }

    @Then("it will show the task {word} as the current one")
    fun step26(taskId: String) = onScenarioContext {
        onNodeWithTag(Tags.CurrentTask).apply {
            assert(hasAnyDescendant(hasText(taskId)))
        }
    }

    @Then("the Resume Task button show not be visible")
    fun step27() = onScenarioContext {
        onNodeWithTag(Tags.ResumeTaskButton).assertDoesNotExist()
    }
}
