package dev.amaro.on_time.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.amaro.on_time.ui.OnTimeTheme
import dev.amaro.on_time.ui.Toolbar
import dev.amaro.on_time.utilities.withTag

@Composable
fun Screen(
    modifier: Modifier = Modifier,
    toolbarContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    OnTimeTheme {
        Surface(color = MaterialTheme.colors.background, modifier = modifier) {
            Column(modifier = withTag("MainScreen")) {
                Toolbar {
                    toolbarContent()
                }
                Column {
                    content()
                }
            }
        }
    }
}