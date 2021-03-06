package dev.amaro.on_time.unit

import assertk.assertThat
import assertk.assertions.isEqualTo
import dev.amaro.on_time.log.Clock
import dev.amaro.on_time.withZeros
import org.junit.Test
import java.time.LocalDateTime

class ClockTest {

    @Test
    fun `Test the clock format`() {
        val now = LocalDateTime.now()
        val result = Clock().nowStr()
        val expected = "${now.year}-${now.monthValue.withZeros(2)}-${now.dayOfMonth.withZeros(2)} ${now.hour.withZeros(2)}:${now.minute.withZeros(2)}"
        assertThat(result).isEqualTo(expected)
    }
}