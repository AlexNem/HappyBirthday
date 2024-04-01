package com.alexnemyr.happybirthday.ui.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.alexnemyr.happybirthday.R
import timber.log.Timber
import java.time.LocalDate
import java.time.Period
import java.util.Calendar
import java.util.Date

enum class BGType {
    FOX, PELICAN//, ELEPHANT //todo: crash with elephant vector drawable
}

data class AnniversaryRes(
    val color: Color,
    val painter: Painter,
    val bntColor: Color,
    val bntBGColor: Color,
    val bntIcon: Painter,
    val bntAddIcon: Painter,
)

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

fun Long.getYearOrMonth(age: Age): String =
    if (age.year == 0) "MONTH"
    else "YEAR"

val Long.age: Age
    get() {
        val birthdayCal: Calendar = Calendar.getInstance().apply { time = Date(this@age) }
        val resultAge = birthdayCal.age
        Timber.tag("Date ->").e("\nresultAge = $resultAge")
        return resultAge
    }

data class NumberIcon(
    val number: Int,
    val iconId: Int
) {
    companion object {
        val list = listOf(
            NumberIcon(number = 0, iconId = R.drawable.number_zero),
            NumberIcon(number = 1, iconId = R.drawable.number_one),
            NumberIcon(number = 2, iconId = R.drawable.number_two),
            NumberIcon(number = 3, iconId = R.drawable.number_three),
            NumberIcon(number = 4, iconId = R.drawable.number_four),
            NumberIcon(number = 5, iconId = R.drawable.number_five),
            NumberIcon(number = 6, iconId = R.drawable.number_six),
            NumberIcon(number = 7, iconId = R.drawable.number_seven),
            NumberIcon(number = 8, iconId = R.drawable.number_eight),
            NumberIcon(number = 9, iconId = R.drawable.number_nine),
        )
    }
}
