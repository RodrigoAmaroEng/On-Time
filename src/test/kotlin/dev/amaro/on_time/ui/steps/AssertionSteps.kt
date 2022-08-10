package dev.amaro.on_time.ui.steps

import androidx.compose.ui.test.*
import assertk.assertThat
import assertk.assertions.isEqualTo
import dev.amaro.on_time.Samples
import dev.amaro.on_time.log.LogEvent
import dev.amaro.on_time.log.Storage
import dev.amaro.on_time.log.StoreItemTask
import io.cucumber.java.en.Then
import io.mockk.CapturingSlot
import io.mockk.slot
import io.mockk.verify
import org.koin.java.KoinJavaComponent.inject

class AssertionSteps : Step {

    @Then("it will show message explaining it needs to be configured")
    fun step1() = onScenarioContext {
        onNodeWithText("No configuration was found").assertExists()
    }

    @Then("show a button to start the configuration")
    fun step2() = onScenarioContext {
        onNodeWithTag("StartConfigurationButton").assertExists()
    }

    @Then("it will display the list of tasks available")
    fun step3() = onScenarioContext {
        onNodeWithTag("QueryResults").assertExists()
    }

    @Then("the filter options will be visible")
    fun step4() = onScenarioContext {
        onNodeWithTag("TaskFilters").assertExists()
    }

    @Then("it will present a message telling there are no tasks available")
    fun step5() = onScenarioContext {
        onNodeWithTag("NoTasksAvailableMessage").assertExists()
    }

    @Then("it will present a message informing it wasn't able to retrieve the tasks")
    fun step6() = onScenarioContext {
        onNodeWithTag("NetworkFailureMessage").assertExists()
    }

    @Then("it will show only my task")
    fun step7() = onScenarioContext {
        onNodeWithTag("QueryResults").apply {
            assertExists()
            onChildren().filterToOne(hasText(Samples.TASK_ID_1)).assertExists()
        }

        onNodeWithText(Samples.TASK_ID_2).assertDoesNotExist()
        onNodeWithText(Samples.TASK_ID_3).assertDoesNotExist()
    }

    @Then("it will show all tasks")
    fun step8() = onScenarioContext {
        onNodeWithTag("QueryResults").apply {
            assertExists()
            onChildren().filterToOne(hasText(Samples.TASK_ID_1)).assertExists()
            onChildren().filterToOne(hasText(Samples.TASK_ID_2)).assertExists()
            onChildren().filterToOne(hasText(Samples.TASK_ID_3)).assertExists()
        }
    }

    @Then("it will show as current task")
    fun step9() = onScenarioContext {
        onNodeWithTag("CurrentTask").apply {
            assert(hasAnyDescendant(hasText(Samples.TASK_ID_1)))
        }
    }

    @Then("register the task start time")
    fun step10() = onScenarioContext {
        val storage: Storage by inject(Storage::class.java)
        val slot: CapturingSlot<StoreItemTask> = slot()
        verify { storage.include(capture(slot)) }
        assertThat(slot.captured.event).isEqualTo(LogEvent.TASK_START)
        assertThat(slot.captured.task).isEqualTo(Samples.task1)
    }

    @Then("the new task will be shown as current task")
    fun step11() = onScenarioContext {
        onNodeWithTag("CurrentTask").apply {
            assert(hasAnyDescendant(hasText(Samples.TASK_ID_2)))
        }
    }

    @Then("register the previous task end time")
    fun step12() = onScenarioContext {
        val storage: Storage by inject(Storage::class.java)
        val slots = mutableListOf<StoreItemTask>()
        verify { storage.include(capture(slots)) }
        assertThat(slots[0].event).isEqualTo(LogEvent.TASK_END)
        assertThat(slots[0].task).isEqualTo(Samples.task1)
    }

    @Then("register the new task start time")
    fun step13() = onScenarioContext {
        val storage: Storage by inject(Storage::class.java)
        val slots = mutableListOf<StoreItemTask>()
        verify { storage.include(capture(slots)) }
        assertThat(slots[1].event).isEqualTo(LogEvent.TASK_START)
        assertThat(slots[1].task).isEqualTo(Samples.task2)
    }

    @Then("I will see no current task")
    fun step14() = onScenarioContext {
        onNodeWithTag("CurrentTask").assertDoesNotExist()
    }

    @Then("register the task end time")
    fun step15() = onScenarioContext {
        val storage: Storage by inject(Storage::class.java)
        val slots = mutableListOf<StoreItemTask>()
        verify { storage.include(capture(slots)) }
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



}