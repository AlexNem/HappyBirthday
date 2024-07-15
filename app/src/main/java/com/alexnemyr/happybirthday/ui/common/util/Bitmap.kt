package com.alexnemyr.happybirthday.ui.common.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun screenshotWrapper(content: @Composable () -> Unit): () -> Bitmap {
    val context = LocalContext.current
    val composeView = remember { ComposeView(context = context) }
    fun captureBitmap(): Bitmap = composeView.drawToBitmap()
    AndroidView(
        factory = {
            composeView.apply {
                setContent {
                    content()
                }
            }
        },
        modifier = Modifier.wrapContentSize(unbounded = false)
    )

    return ::captureBitmap
}

fun share(bitmap: Bitmap, context: Context) {
    val timeStamp = SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault()).format(Date())
    val bitmapPath: String =
        MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, timeStamp, null)
    val bitmapUri = Uri.parse(bitmapPath)

    val intent = Intent(Intent.ACTION_SEND)
    intent.setType("image/png")
    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
    val chooser = Intent.createChooser(intent, CHOOSER_TITLE)

    context.startActivity(chooser)
}

const val CHOOSER_TITLE = "Share"
