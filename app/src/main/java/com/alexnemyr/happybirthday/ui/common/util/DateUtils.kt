package com.alexnemyr.happybirthday.ui.common.util

import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber
import java.time.LocalDate
import java.time.Period
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun toDate(age: Long?): Age {
    return age?.let {
        val birthdayCal: Calendar = Calendar.getInstance().apply { time = Date(it) }
        val resultAge = birthdayCal.age
        Timber.tag("Date ->").e("\nresultAge = $resultAge")
        resultAge
    } ?: Age(0, 0)
}

data class Age(
    val year: Int,
    val month: Int
)

val Calendar.age: Age
    get() {
        val year = this.get(Calendar.YEAR)
        val month = this.get(Calendar.MONTH) + 1
        val dayOfMonth = this.get(Calendar.DAY_OF_MONTH)
        return Age(
            year = Period.between(
                LocalDate.of(year, month, dayOfMonth),
                LocalDate.now()
            ).years,
            month = Period.between(
                LocalDate.of(year, month, dayOfMonth),
                LocalDate.now()
            ).months
        )
    }