package com.alexnemyr.happybirthday

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexnemyr.happybirthday.ui.common.Age
import com.alexnemyr.happybirthday.ui.common.age
import com.alexnemyr.happybirthday.ui.common.BirthdayState
import com.alexnemyr.repository.UserRepository
import com.alexnemyr.repository.domain.UserDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import java.util.Date

class BirthdayViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val mutableState: MutableStateFlow<BirthdayState> = MutableStateFlow(

        BirthdayState(
            capturedImageUri = mutableStateOf(Uri.EMPTY), //Uri.parse(userRepository.user.uri)
            name = mutableStateOf(userRepository.user.name ?: ""),
            date = mutableLongStateOf(userRepository.user.date?.toLongOrNull() ?: 2)
        )
    )
    val state: StateFlow<BirthdayState> = mutableState

    val navigationState: MutableStateFlow<Boolean> = MutableStateFlow(true)

    init {
        val repo = userRepository.user
        Timber.tag(TAG).e("InputViewModel -> init $repo")
    }

    fun saveInfo(
        name: String,
        date: String,
        uri: String
    ) {
        userRepository.saveUser(
            UserDomain(
                name = name,
                date = date,
                uri = uri
            )
        )
    }

    fun navTo(value: Boolean) {
        viewModelScope.launch {
            navigationState.emit(value)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toDate(age: Long?): Age {
        return age?.let {
            val birthdayCal: Calendar = Calendar.getInstance().apply { time = Date(it) }
            val resultAge = birthdayCal.age
            Timber.tag("Date ->").e("\nresultAge = $resultAge")
            resultAge
        } ?: Age(0, 0)
    }
}