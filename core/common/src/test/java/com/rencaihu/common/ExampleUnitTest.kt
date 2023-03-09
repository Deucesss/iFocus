package com.rencaihu.common

import com.rencaihu.common.ext.TimeUtil
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testSecondsInSeconds() {
        val seconds = TimeUtil.secondsInSeconds(7000)
        val minutes = TimeUtil.minuteInSeconds(7000)
        val hours = TimeUtil.hoursInSeconds(7000)
        assertEquals(hours, 1)
        assertEquals(minutes, 56)
        assertEquals(seconds, 40)
    }
}
