package com.alexnemyr.happybirthday

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexnemyr.happybirthday.ui.flow.ScreenshotWrapper
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
                    Box(modifier = Modifier) {
                        val screenshotableComposable = ScreenshotWrapper(

                            content = {
                                AnniversaryScreen(viewModel)
                            }
                        )
                        val onClick = {
                            val bitmap = screenshotableComposable.invoke()
                            share(bitmap)
                            Timber.tag("NAVIG").i("bitmap = ${bitmap}")
                        }
                        Button(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp),
                            onClick = onClick
                        ) {
                            Text(text = "Share the news")
                        }
                    }
                }
                Timber.tag("NAVIG").i("navState = ${navState.value}")

            }
        }
    }

    private fun share(bitmap: Bitmap) {
        val bitmapPath: String =
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, "title", null)
        val bitmapUri = Uri.parse(bitmapPath)

        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("image/png")
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        val chooser = Intent.createChooser(intent, "Share")

        startActivity(chooser)
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