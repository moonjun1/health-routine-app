package com.example.gymroutine.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Extension functions for common operations
 */

// String extensions
fun String.isValidEmail(): Boolean {
    return this.matches(Regex(Constants.EMAIL_PATTERN))
}

fun String.isValidPassword(): Boolean {
    return this.length >= Constants.MIN_PASSWORD_LENGTH
}

// Long (timestamp) extensions
fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN)
    return sdf.format(Date(this))
}

fun Long.toFormattedTime(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.KOREAN)
    return sdf.format(Date(this))
}

fun Long.toFormattedDateTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN)
    return sdf.format(Date(this))
}

// Day of week extensions
fun getDayOfWeekKey(dayOfWeek: Int): String {
    return when (dayOfWeek) {
        Calendar.MONDAY -> "mon"
        Calendar.TUESDAY -> "tue"
        Calendar.WEDNESDAY -> "wed"
        Calendar.THURSDAY -> "thu"
        Calendar.FRIDAY -> "fri"
        Calendar.SATURDAY -> "sat"
        Calendar.SUNDAY -> "sun"
        else -> "mon"
    }
}

fun getDayOfWeekName(key: String): String {
    return when (key) {
        "mon" -> "월요일"
        "tue" -> "화요일"
        "wed" -> "수요일"
        "thu" -> "목요일"
        "fri" -> "금요일"
        "sat" -> "토요일"
        "sun" -> "일요일"
        else -> "월요일"
    }
}
