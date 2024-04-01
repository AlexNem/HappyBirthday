package com.alexnemyr.happybirthday.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun Photo(state: BirthdayState, painter: Painter, modifier: Modifier) {
    Box(modifier) {
        if (state.capturedImageUri.value.path?.isNotEmpty() == true) {
            val request =
                ImageRequest.Builder(LocalContext.current)
                    .data(state.capturedImageUri.value)
                    .allowHardware(false)
                    .build()
            AsyncImage(
                model = request,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .size(292.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(photoSize),
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }

}