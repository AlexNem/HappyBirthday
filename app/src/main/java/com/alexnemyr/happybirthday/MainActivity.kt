package com.alexnemyr.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alexnemyr.happybirthday.navigation.BirthdayNavHost
import com.alexnemyr.happybirthday.ui.common.util.Permission
import com.alexnemyr.happybirthday.ui.theme.AppTheme
import com.alexnemyr.happybirthday.ui.theme.darkSystemBarStyle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge(statusBarStyle = darkSystemBarStyle)
        setContent {
            AppTheme {
                BirthdayNavHost()
                Permission()
            }
        }
    }
}
