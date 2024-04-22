package com.alexnemyr.happybirthday.ui.common.util

import com.alexnemyr.domain.util.TAG
import com.alexnemyr.happybirthday.R
import timber.log.Timber
import java.util.Calendar
import java.util.Date

enum class BGType {
    FOX, PELICAN//, ELEPHANT //todo: crash with elephant vector drawable
}

val BGType.resources: AnniversaryResources
    get() {
        return when (this) {
            BGType.FOX -> AnniversaryResources(
                backgroundColor = R.color.bg_fox,
                backgroundDrawable = R.drawable.bg_fox,
                btnColor = R.color.btn_fox,
                btnBGColor = R.color.btn_bg_fox,
                btnIcon = R.drawable.ic_smile_fox,
                btnAddIcon = R.drawable.ic_add_fox,
            )

            BGType.PELICAN -> AnniversaryResources(
                backgroundColor = R.color.bg_pelican,
                backgroundDrawable = R.drawable.bg_pelican,
                btnColor = R.color.btn_pelican,
                btnBGColor = R.color.btn_bg_pelican,
                btnIcon = R.drawable.ic_smile_pelican,
                btnAddIcon = R.drawable.ic_add_pelican,
            )
        }
    }

fun getAnniversaryResources(): AnniversaryResources {
    val bgList = listOf(BGType.FOX, BGType.PELICAN)
    return bgList.random().resources
}

data class AnniversaryResources(
    val backgroundColor: Int,
    val backgroundDrawable: Int,
    val btnColor: Int,
    val btnBGColor: Int,
    val btnIcon: Int,
    val btnAddIcon: Int,
)


fun String.getYearOrMonth(age: Age): String =
    if (age.year == 0) "MONTH"
    else "YEAR"

val String.age: Age
    get() {
        val birthdayCal: Calendar = Calendar.getInstance().apply { time = Date(this@age) }
        val resultAge = birthdayCal.age
        Timber.tag(TAG).d("\nresultAge = $resultAge")
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
