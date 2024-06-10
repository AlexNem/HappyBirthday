package com.alexnemyr.happybirthday.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexnemyr.happybirthday.ui.flow.anniversary.AnniversaryBitmapWrapperScreen
import com.alexnemyr.happybirthday.ui.flow.anniversary.AnniversaryViewModel
import com.alexnemyr.happybirthday.ui.flow.input.InputScreen
import com.alexnemyr.happybirthday.ui.flow.input.InputViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun BirthdayNavHost() {

    val anniversaryViewModel: AnniversaryViewModel = koinViewModel<AnniversaryViewModel>()
    val inputViewModel: InputViewModel = koinViewModel<InputViewModel>()

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.InputScreen.name) {
        composable(route = Screen.InputScreen.name) {
            InputScreen(
                inputViewModel = inputViewModel,
                navController = navController
            )
        }
        composable(route = Screen.AnniversaryBitmapWrapperScreen.name) {
            AnniversaryBitmapWrapperScreen(
                viewModel = anniversaryViewModel,
                navController = navController
            )
        }

    }

}