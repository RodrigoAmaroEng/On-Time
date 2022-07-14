package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun MessageBox(
    icon: String,
    message: String,
    title: String? = null,
    action: String? = null,
    modifier: Modifier = Modifier,
    onAction: () -> Unit = {}
) {

    Row(
        modifier.border(1.dp, MaterialTheme.colors.secondary, RectangleShape)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(icon), "", modifier = Modifier.size(64.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            title?.let {
                Text(it, style = MaterialTheme.typography.subtitle1)
                Spacer(Modifier.height(8.dp))
            }
            Text(message, style = MaterialTheme.typography.body2)
            action?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, style = MaterialTheme.typography.button, modifier = Modifier.clickable { onAction() })
            }
        }
    }
}

@Composable
@Preview
fun previewMessageBox() {
    OnTimeTheme {
        Column {
            MessageBox(Icons.ON_QA, "Some message")
            Spacer(Modifier.height(12.dp))
            MessageBox(Icons.ON_QA, "Some message", "Some title")
            Spacer(Modifier.height(12.dp))
            MessageBox(Icons.ON_QA, "Some message", "Some title", "Action")
        }
    }
}