package com.alexnemyr.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alexnemyr.happybirthday.ui.flow.anniversary.AnniversaryScreen
import com.alexnemyr.happybirthday.ui.flow.input.InputScreen
import com.alexnemyr.happybirthday.ui.theme.HappyBirthdayTheme
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val viewModel: BirthdayViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HappyBirthdayTheme {
                val navState = viewModel.navigationState.collectAsState()
                if (navState.value) {
                    InputScreen(viewModel)
                } else {
                    AnniversaryScreen(viewModel)
                }
                Timber.tag("NAVIG").i("navState = ${navState.value}")

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HappyBirthdayTheme {
        Greeting("Android")
    }
}