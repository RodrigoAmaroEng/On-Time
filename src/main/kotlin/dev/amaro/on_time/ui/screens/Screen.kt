package dev.amaro.on_time.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.ui.Icons
import dev.amaro.on_time.ui.MessageBox
import dev.amaro.on_time.ui.OnTimeTheme
import dev.amaro.on_time.ui.Toolbar

@Composable
fun Screen(
    state: AppState,
    modifier: Modifier = Modifier,
    toolbarContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    OnTimeTheme {
        Surface(color = MaterialTheme.colors.background, modifier = modifier) {
            Column {
                Toolbar {
                    toolbarContent()
                }
                Column {
                    content()
                }

            }
            state.feedback?.run {
                MessageBox(
                    Icons.ON_QA,
                    title = title,
                    message = body,
                )
            }
        }
    }
}