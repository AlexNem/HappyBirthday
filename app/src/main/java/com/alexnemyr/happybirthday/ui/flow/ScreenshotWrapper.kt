package com.alexnemyr.happybirthday.ui.flow

import android.graphics.Bitmap
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap

@Composable
fun ScreenshotWrapper(content : @Composable () -> Unit): () -> Bitmap {
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