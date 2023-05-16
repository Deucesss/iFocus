package com.rencaihu.common.ext

import org.junit.Test


class TimeUtilTest {

    @Test
    fun getHoursInSecond_lessThanOneHour_returnsZero() {
        val timeInSeconds = 3599

        val result = TimeUtil.hoursInSeconds(timeInSeconds)

        assert(result == 0)
    }

    @Test
    fun getHoursInSecond_moreThanOneHour_returnsHours() {
        val timeInSeconds = 3600

        val result = TimeUtil.hoursInSeconds(timeInSeconds)

        assert(result == 1)
    }

    @Test
    fun getHoursInSecond_moreThanOneHourLessThanTwoHour_returnsOne() {
        val timeInSeconds = 7199

        val result = TimeUtil.hoursInSeconds(timeInSeconds)

        assert(result == 1)
    }

    @Test
    fun getMinutesInSecond_lessThanOneMinute_returnsZero() {
        val timeInSeconds = 59

        val result = TimeUtil.minuteInSeconds(timeInSeconds)

        assert(result == 0)
    }

    @Test
    fun getMinutesInSecond_moreThanOneMinuteLessThanOneHour_returnsMinutes() {
        val timeInSeconds = 60

        val result = TimeUtil.minuteInSeconds(timeInSeconds)

        assert(result == 1)
    }

    @Test
    fun getMinutesInSecond_moreThanOneHourLessThanTwoHours_returnMinutes() {
        val timeInSeconds = 7199

        val result = TimeUtil.minuteInSeconds(timeInSeconds)

        assert(result == 59)
    }

    @Test
    fun getSecondsInSecond_lessThanOneMinute_returnsSeconds() {
        val timeInSeconds = 59

        val result = TimeUtil.secondsInSeconds(timeInSeconds)

        assert(result == 59)
    }

    @Test
    fun getSecondsInSecond_moreThanOneMinuteLessThanTwoMinute_returnsSeconds() {
        val timeInSeconds = 60

        val result = TimeUtil.secondsInSeconds(timeInSeconds)

        assert(result == 0)
    }

    @Test
    fun getSecondsInSecond_moreThanOneHourLessThanTwoHours_returnsSeconds() {
        val timeInSeconds = 7199

        val result = TimeUtil.secondsInSeconds(timeInSeconds)

        assert(result == 59)
    }
}