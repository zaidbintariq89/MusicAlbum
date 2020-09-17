package com.bell.youtubeplayer.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    private const val EVENT_CREATE_TIME_FORMAT = "hh:mm"

    fun getTimeHMM(timestamp: Long): String {
        try {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            val sdf = SimpleDateFormat(EVENT_CREATE_TIME_FORMAT, Locale.US)
            val currentTimeZone = calendar.time
            return sdf.format(currentTimeZone)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun parseVideoTime(durationString: String): String {
        //PT4M15S
        var length = ""
        var durationText = durationString
        if (!TextUtils.isEmpty(durationText)) {
            durationText = durationText.substring(2, durationText.length)

            val isHourPresent: Boolean = durationText.contains("H")
            val isMinutePresent: Boolean = durationText.contains("M")
            val isSecondPresent: Boolean = durationText.contains("S")

            var duration = durationText.replace("H", ":")
            duration = duration.replace("M".toRegex(), ":")
            duration = duration.replace("S".toRegex(), ":")
            var hourToken = ""
            var minuteToken = ""
            var secondToken = ""
            val tokens = duration.split(":".toRegex()).toTypedArray()
            when {
                isHourPresent -> {
                    hourToken = tokens[0]
                    minuteToken = if (isMinutePresent) {
                        ":" + formatToken(tokens[1])
                    } else {
                        ":00"
                    }
                    secondToken = if (isSecondPresent) {
                        ":" + formatToken(tokens[2])
                    } else {
                        ":00"
                    }
                }
                isMinutePresent -> {
                    minuteToken = tokens[0]
                    secondToken = if (isSecondPresent) {
                        ":" + formatToken(tokens[1])
                    } else {
                        ":00"
                    }
                }
                isSecondPresent -> {
                    secondToken = "0:" + formatToken(tokens[0])
                }
            }
            length = hourToken + minuteToken + secondToken
        }

        return length
    }

    private fun formatToken(tokenStr: String): String {
        var token = tokenStr
        if (token.length < 2) {
            token = "0$token"
        }
        return token
    }
}