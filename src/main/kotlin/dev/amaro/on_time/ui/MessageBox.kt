package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun MessageBox(
    icon: String,
    message: String,
    title: String? = null,
    actionText: String? = null,
    modifier: Modifier = Modifier,
    onAction: () -> Unit = {}
) {
    Box(
        Modifier.fillMaxSize()
            .background(Color(0,0,0,100)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier
                .width(IntrinsicSize.Max)
                .border(2.dp, MaterialTheme.extension.blackDark, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
                .padding(Theme.Dimens.Margins.LARGE),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                "",
                tint = MaterialTheme.extension.blackOverSecondary,
                modifier = Modifier.size(Theme.Dimens.Icons.EXTRA_LARGE)
            )
            Spacer(Modifier.width(Theme.Dimens.Spacing.LARGE))
            Column {
                title?.let {
                    Text(
                        text= it,
                        style = MaterialTheme.extension.title,
                        color = MaterialTheme.colors.onSurface
                    )
                    Spacer(Modifier.height(Theme.Dimens.Spacing.MEDIUM))
                }
                Text(
                    text = message,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.body1
                )
                actionText?.let {
                    Spacer(Modifier.height(Theme.Dimens.Spacing.LARGE))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.extension.blackOverPrimary,
                        modifier = Modifier.fillMaxWidth().clickable { onAction() }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun previewMessageBox() {
    NewOnTimeTheme {
        Column {

            MessageBox(Icons.ON_QA, "Some message", "Some title", "Action")
        }
    }
}