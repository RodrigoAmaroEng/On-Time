package dev.amaro.on_time.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import dev.amaro.on_time.utilities.Constants
import dev.amaro.on_time.utilities.toHoursFormat
import dev.amaro.on_time.utilities.toMinutesFormat
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun ClockDisplay(initial: LocalDateTime, icon: String? = null, modifier: Modifier = Modifier) {
    val startedAt = remember { derivedStateOf { initial } }
    fun elapsed(since: LocalDateTime): String {
        return Duration.between(since, LocalDateTime.now()).let {
            "${it.toHoursFormat()}:${it.toMinutesFormat()}"
        }
    }

    val time = remember { mutableStateOf(elapsed(initial)) }
    LaunchedEffect(0) { // 3
        while (true) {
            time.value = elapsed(startedAt.value)
            delay(Constants.ONE_SECOND)
        }
    }
    Row (verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        icon?.let {
            Icon(
                painter = painterResource(it),
                Constants.EMPTY,
                modifier = Modifier.size(Theme.Dimens.Icons.TINY)
            )
            Spacer(modifier = Modifier.width(Theme.Dimens.Margins.SMALL))
        }
        Text(time.value, style = MaterialTheme.extension.small)
    }
}



