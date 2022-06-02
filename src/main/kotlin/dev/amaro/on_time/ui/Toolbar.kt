package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.utilities.Constants

@Composable
fun Toolbar(vararg buttons: ButtonDef) {
    Surface(Modifier.fillMaxWidth().height(64.dp), color = MaterialTheme.colors.secondary) {
        Row {
            buttons.forEach {
                it.apply { SquareButton(imageResource) }
            }
        }
    }
}

@Preview
@Composable
fun previewToolbar() {
    OnTimeTheme {
        Toolbar(
            ButtonDef("images/ic_user_assign.svg"),
            ButtonDef("images/ic_done.svg")
        )
    }
}

@Composable
fun SquareButton(resource: String) {
    Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) {
        Image(
            painterResource(resource),
            Constants.EMPTY,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant),
            modifier = Modifier.size(32.dp)
        )
    }
}

data class ButtonDef(
    val imageResource: String
)