package com.alexnemyr.happybirthday.ui.common.element.image

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alexnemyr.domain.util.TAG
import com.alexnemyr.happybirthday.ui.common.photoSize
import com.alexnemyr.happybirthday.ui.common.util.fileFromContentUri
import com.alexnemyr.happybirthday.ui.common.util.getUri
import kotlinx.coroutines.launch
import timber.log.Timber


@Composable
fun Photo(
    uriPath: String?,
    painter: Painter,
    modifier: Modifier
) {

    Timber.tag(TAG).e("Photo -> uriPath = $uriPath")
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val selectedImageState: MutableState<Uri?> = remember {
        mutableStateOf(null)
    }


    SideEffect {
        scope.launch {
            selectedImageState.value = getUri(uriPath, context)
        }
    }

    Box(modifier) {
        if (!selectedImageState.value?.path.isNullOrBlank()) {
            selectedImageState.value?.let {
                val fileFromContentUri = fileFromContentUri(context, it)
                val absolutePath = fileFromContentUri.absolutePath
                Timber.tag(TAG).e("Photo -> absolutePath = $absolutePath")
                val request =
                    ImageRequest.Builder(context).data(Uri.parse(absolutePath)).allowHardware(false)
                        .build()

                val clipPercent = 100
                AsyncImage(
                    model = request,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(clipPercent))
                        .size(292.dp),
                    contentScale = ContentScale.Crop
                )
            }

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
