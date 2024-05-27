package com.alexnemyr.happybirthday.ui.flow.anniversary

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.alexnemyr.happybirthday.ui.flow.screenshotWrapper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AnniversaryBitmapWrapperScreen(
    viewModel: AnniversaryViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    Box(modifier = Modifier) {
        val bitmapWrapper = screenshotWrapper(
            content = {
                AnniversaryScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        )
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            onClick = {
                val bitmap = bitmapWrapper.invoke()
                share(bitmap, context)
            }
        ) {
            Text(text = "Share the news")
        }
    }

}

private fun share(bitmap: Bitmap, context: Context) {
    val timeStamp = SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault()).format(Date())
    val bitmapPath: String =
        MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, timeStamp, null)
    val bitmapUri = Uri.parse(bitmapPath)

    val intent = Intent(Intent.ACTION_SEND)
    intent.setType("image/png")
    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
    val chooser = Intent.createChooser(intent, "Share")

    context.startActivity(chooser)
}
