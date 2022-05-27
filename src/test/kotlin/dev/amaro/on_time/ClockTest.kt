package dev.amaro.on_time

import dev.amaro.on_time.log.Clock
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class ClockTest {

    fun Int.withZeros(n: Int) = this.toString().padStart(n, '0')

    @Test
    fun `Test the clock format`() {
        val now = LocalDateTime.now()
        val result = Clock().nowStr()
        val expected = "${now.year}-${now.monthValue.withZeros(2)}-${now.dayOfMonth.withZeros(2)} ${now.hour.withZeros(2)}:${now.minute.withZeros(2)}"
        assertEquals(expected, result)
    }
}