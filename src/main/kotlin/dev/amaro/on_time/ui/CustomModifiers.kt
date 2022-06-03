package dev.amaro.on_time.ui

import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize


@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.onHover(onHover: (size: IntSize, pos: Offset, isOver: Boolean) -> Unit) =
    onPointerEvent(
        eventType = PointerEventType.Move,
        onEvent = {
            onHover(size, it.changes.last().position, true)
        }
    ).onPointerEvent(
        eventType = PointerEventType.Exit,
        onEvent = {
            onHover(size, it.changes.first().position, false)
        }
    )


fun Modifier.onHover(portion: Portion, onHover: (isOver: Boolean) -> Unit) =
    onHover { size, pos, isOver ->
        onHover(isOver && portion.isOver(size, pos))
    }

data class Portion(
    val alignment: Alignment,
    val percentSize: IntSize = IntSize(30, 30)
) {
    fun isOver(size: IntSize, pos: Offset): Boolean {
        val areaWidth =  (size.width * percentSize.width / 100)
        val areaHeight = (size.height * percentSize.height / 100)

        val point = when(alignment) {
            Alignment.Center -> IntOffset((size.width - areaWidth) / 2, (size.height  - areaHeight) / 2)
            Alignment.CenterStart -> IntOffset(0, (size.height - areaHeight) / 2)
            Alignment.CenterEnd -> IntOffset(size.width - areaWidth, (size.height - areaHeight) / 2)
            Alignment.TopStart -> IntOffset(0, 0)
            Alignment.TopCenter -> IntOffset((size.width - areaWidth) / 2, 0)
            Alignment.TopEnd -> IntOffset(size.width - areaWidth, 0)
            Alignment.BottomStart -> IntOffset(0, size.height - areaHeight)
            Alignment.BottomCenter -> IntOffset((size.width - areaWidth) / 2, size.height - areaHeight)
            Alignment.BottomEnd -> IntOffset(size.width - areaWidth, size.height - areaHeight)
            else -> IntOffset(0, 0)
        }
        val area = Rect(point.toOffset(), IntSize(areaWidth, areaHeight).toSize())
        return area.contains(pos)
    }
}