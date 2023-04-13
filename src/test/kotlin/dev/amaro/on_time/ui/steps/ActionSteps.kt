
package dev.amaro.on_time.ui.steps

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.*
import dev.amaro.on_time.Samples
import dev.amaro.on_time.Samples.HOST
import dev.amaro.on_time.Samples.TOKEN
import dev.amaro.on_time.Samples.USER
import dev.amaro.on_time.ui.RunCucumberTest
import dev.amaro.on_time.ui.Tags
import dev.amaro.on_time.ui.TextResources
import io.cucumber.java.en.When
import kotlinx.coroutines.test.advanceTimeBy


@OptIn(ExperimentalTestApi::class)
class ActionSteps : Step {

    @When("I start the App")
    @When("this task is rendered")
    fun step1() {
        RunCucumberTest.startApp()
    }

    @When("I press the Filter Assigned to Me button")
    fun step2() = onScenarioContext {
        onNodeWithTag(Tags.FilterMineButton).performClick()
    }

    @When("I select a task")
    fun step3() = onScenarioContext {
        onNodeWithText(Samples.TASK_ID_1).performClick()
    }

    @When("I select another task")
    fun step4() = onScenarioContext {
        onNodeWithText(Samples.TASK_ID_2).performClick()
    }

    @When("I press the task finish button")
    fun step5() = onScenarioContext {
        onNodeWithTag(Tags.StopWorkingButton).performClick()
    }


    @When("I move the cursor over the task options")
    fun step6() = onScenarioContext {
        onNodeWithTag("Task-CST-123").apply {
            performMouseInput {
                enter(Offset(0.8f, 0.5f))
            }
        }
    }

    @When("I press the Start Configuration button")
    fun step7() = onScenarioContext {
        onNodeWithText(TextResources.Actions.StartConfiguration).performClick()
    }

    @When("I fill all settings")
    fun step8() = onScenarioContext {
        onNodeWithTag(Tags.Settings_Prop_Host + "_Value").performTextInput(HOST)
        onNodeWithTag(Tags.Settings_Prop_User + "_Value").performTextInput(USER)
        onNodeWithTag(Tags.Settings_Prop_Token + "_Value").performTextInput(TOKEN)
    }

    @When("I press the save button")
    fun step9() = onScenarioContext {
        onNodeWithTag(Tags.SaveButton).performClick()
    }

    @When("I press the Settings button")
    fun step10() = onScenarioContext {
        onNodeWithTag(Tags.SettingsTab).performClick()
    }

    @When("I press the pomodoro start button")
    fun step11() = onScenarioContext {
        onNodeWithTag(Tags.CurrentTask)
            .onChildren()
            .filterToOne(hasTestTag(Tags.StartPomodoroButton))
            .performClick()
    }

    @When("I wait {long} minutes")
    fun step12(minutes: Long) = onScenarioContext {
        RunCucumberTest.clockScope.advanceTimeBy(minutes * 60 * 1000 + 50)
    }

    @When("I press the Resume Task button")
    fun step13() = onScenarioContext {
        onNodeWithTag(Tags.ResumeTaskButton).performClick()
    }
}