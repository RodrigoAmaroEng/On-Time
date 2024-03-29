package dev.amaro.on_time.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.Navigation
import dev.amaro.on_time.ui.*
import dev.amaro.on_time.utilities.Constants
import dev.amaro.on_time.utilities.withTag

@Composable
fun Screen(
    state: AppState,
    onAction: (Actions) -> Unit,
    modifier: Modifier = Modifier,
    toolbarContent: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    NewOnTimeTheme {
        Box(modifier) {
            Row {
                Surface(color = MaterialTheme.colors.primary) {
                    Column(Modifier.weight(1f).fillMaxHeight().width(IntrinsicSize.Max)) {
                        Image(
                            painterResource(Images.Logo),
                            Constants.EMPTY,
                            Modifier
                                .padding(Theme.Dimens.Spacing.LARGE)
                                .height(Theme.Dimens.Icons.MEDIUM)
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(Modifier.height(Theme.Dimens.Height.REGULAR))
                        Navigation.values().forEach {
                            NavigationTab(it.icon, it.title, state.screen == it, withTag(it.tag)) {
                                onAction(it.action)
                            }
                        }
                    }
                }
                Surface(color = MaterialTheme.extension.blackBase) {
                    Column(Modifier.weight(4f)) {
                        Toolbar(toolbarContent)
                        content()
                    }
                }
            }

            state.feedback?.run {
                MessageBox(
                    Icons.ON_QA,
                    title = title,
                    message = body,
                    actionText = TextResources.Actions.Dismiss,
                    onAction = { onAction(Actions.DismissFeedback) }
                )
            }
        }
    }
}