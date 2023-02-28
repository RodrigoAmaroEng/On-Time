package dev.amaro.on_time.unit

import assertk.assertThat
import assertk.assertions.isEqualTo
import dev.amaro.on_time.log.Clock
import dev.amaro.on_time.withZeros
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.*
import kotlin.test.Test
import java.time.LocalDateTime

class ClockTest {

    @Test
    fun `Test the clock format`() {
        val now = LocalDateTime.now()
        val result = Clock().nowStr()
        val expected = "${now.year}-${now.monthValue.withZeros(2)}-${now.dayOfMonth.withZeros(2)} ${now.hour.withZeros(2)}:${now.minute.withZeros(2)}"
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Start a 1 minute timer`() = runTest(UnconfinedTestDispatcher()) {
        val callback : () -> Unit = mockk(relaxed = true)
        Clock(this).startTimer(1, callback )
        advanceTimeBy(60001)
        runCurrent()
        verify { callback() }
    }

    @Test
    fun `Start a 1 minute timer - not completed`() = runTest(UnconfinedTestDispatcher()) {
        val callback : () -> Unit = mockk(relaxed = true)
        Clock(this).startTimer(1, callback )
        advanceTimeBy(59001)
        runCurrent()
        verify(exactly = 0) { callback() }
    }

    @Test
    fun `Start a 5 minute timer`() = runTest(UnconfinedTestDispatcher()) {
        val callback : () -> Unit = mockk(relaxed = true)
        Clock(this).startTimer(5, callback)
        advanceTimeBy(300001)
        runCurrent()
        verify { callback() }
    }

    @Test
    fun `Start a 5 minute timer - not completed`() = runTest(UnconfinedTestDispatcher()) {
        val callback : () -> Unit = mockk(relaxed = true)
        Clock(this).startTimer(5, callback)
        advanceTimeBy(299900)
        runCurrent()
        verify(exactly = 0) { callback() }
    }

    @Test
    fun `Start starting another timer before the last one stops`() = runTest(UnconfinedTestDispatcher()) {
        val callback : () -> Unit = mockk(relaxed = true)
        Clock(this).apply {
            startTimer(2, callback)
            advanceTimeBy(10 * 1000)
            startTimer(1, callback)
            advanceTimeBy(5 * 60 * 1000)
            runCurrent()
        }
        verify(exactly = 1) { callback() }
    }

    @Test
    fun `Start two sequential timers`() = runTest(UnconfinedTestDispatcher()) {
        val callback : () -> Unit = mockk(relaxed = true)
        Clock(this ).apply {
            startTimer(1, callback )
            advanceTimeBy(1 * 60 * 1001)
            startTimer(1, callback)
            advanceTimeBy(1 * 60 * 1001)
            runCurrent()
        }
        verify(exactly = 2) { callback() }
    }

    @Test
    fun `Stop a running timer`() = runTest(UnconfinedTestDispatcher()) {
        val callback : () -> Unit = mockk(relaxed = true)
        Clock(this).apply {
            startTimer(2, callback)
            advanceTimeBy(1 * 60 * 1001)
            stopTimer()
            advanceTimeBy(1 * 60 * 1001)
            runCurrent()
        }
        verify(exactly = 0) { callback() }
    }
}