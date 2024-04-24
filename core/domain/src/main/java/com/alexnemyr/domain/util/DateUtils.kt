package com.alexnemyr.domain.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun toDate(age: String): Age? {
    val result = runCatching {
        val birthdayCal: Calendar = Calendar.getInstance().apply {
            time = Date(age.toLong())
        }
        val resultAge = birthdayCal.age
        resultAge
    }
        .onSuccess { return it  }
        .onFailure { return null }
    return result.getOrNull()
}

val String.formattedDate: String
    get() {
        runCatching {
            val time = this.toLongOrNull()
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return formatter.format(time)
        }.getOrElse { return this }
    }



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

val Age.yearOrMonthTitle: String
    get() {
        return if (this.year == 0) "MONTH"
        else "YEAR"
    }


val String.age: Age?
    get() {
        val result = runCatching {
            val birthdayCal: Calendar = Calendar.getInstance().apply {
                time = Date(this@age.toLong())
            }
            birthdayCal.age
        }.onSuccess { return it }
            .onFailure { return null }
        return result.getOrNull()
    }