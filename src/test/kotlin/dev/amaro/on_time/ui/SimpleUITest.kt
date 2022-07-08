package dev.amaro.on_time.ui

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.ui.screens.MainScreen
import io.kotest.core.spec.style.FunSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.swing.Swing
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.runner.Description
import org.junit.runners.model.Statement

//@ExtendWith(ComposeRuleExtension::class)
//class SimpleUITest {
//
//    @Test
//    @DisplayName("When using Valid data")
//    fun test(runner: ComposeRuleRunner) = runner.run {
//        setContent {
//            MainScreen(AppState()) {}
//        }
//    }
//
//}

class ComposeRuleExtension : ParameterResolver {

    private val rule by lazy { createComposeRule() }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == ComposeRuleRunner::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return ComposeRuleRunner(
            rule,
            Description.createTestDescription(
                extensionContext.testClass.orElse(this::class.java),
                extensionContext.displayName
            )
        )
    }
}

class ComposeRuleRunner(private val rule: ComposeContentTestRule, private val description: Description) {
    fun run(action: suspend ComposeContentTestRule.() -> Unit) {
        rule.run {
            apply(BlockingComposeStatement(action, rule), description)
        }.evaluate()
    }
}

class BlockingComposeStatement(
    private val action: suspend ComposeContentTestRule.() -> Unit,
    private val rule: ComposeContentTestRule
) : Statement() {
    override fun evaluate() = runBlocking(Dispatchers.Swing) {
        action.invoke(rule)
    }
}

class MyTests : FunSpec({
    val composeRule = createComposeRule()

    fun startCompose(description: Description, action: suspend ComposeContentTestRule.() -> Unit) {
        composeRule.run {
            apply(BlockingComposeStatement(action, composeRule), description)
        }.evaluate()
    }

    test("my first test") {
        val description = Description.createTestDescription(
            testCase::class.java,
            testCase.name.testName
        )
        startCompose(description) {
            setContent {
                MainScreen(AppState()) {}
            }
            onNodeWithTag("MainScreen").assertExists()
            awaitIdle()
        }
    }


})

