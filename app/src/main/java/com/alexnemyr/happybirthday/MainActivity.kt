package com.alexnemyr.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alexnemyr.happybirthday.navigation.BirthdayNavHost
import com.alexnemyr.happybirthday.ui.common.util.Permission
import com.alexnemyr.happybirthday.ui.theme.HappyBirthdayTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HappyBirthdayTheme {
                BirthdayNavHost()
                Permission()
            }
        }
    }
}