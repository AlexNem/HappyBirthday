package com.alexnemyr.happybirthday.ui.common.element.picker

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.alexnemyr.happybirthday.ui.common.element.PrimaryBottomSheet
import com.alexnemyr.happybirthday.ui.common.util.fileFromContentUri
import com.alexnemyr.happybirthday.ui.common.util.getMediaFile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun PicturePicker(
    onSelectPicture: (path: String) -> Unit,
    onSelectUri: (uri: Uri) -> Unit,
    onClosePicker: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //TODO: need to refactor
    val processUri: (uri: Uri) -> Unit = { uri ->
        scope.launch {
            delay(2000) // why delay?
            // All Media.kt file functions should be distributed among viewmodels/repos/datasources OR some other factory classes.
            // rememberCoroutineScope together with getMediaFile() will be canceled if current composable leaves composition.
            val getMediaFile = getMediaFile(context, uri)
            Timber.e("CameraPicker -> getFileName = $getMediaFile")
        }
    }

    PrimaryBottomSheet(
        onDismiss = { onClosePicker() },
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                PhotoPicker(onSelect = { uri ->
                    Timber.e("PhotoPicker -> uri = ${uri}")
                    val absolutePath = fileFromContentUri(context, uri).absolutePath
                    Timber.e("PhotoPicker -> getImages = ${absolutePath}")
                    val getMediaFile = getMediaFile(context, uri)
                    Timber.e("PhotoPicker -> getFileName = ${getMediaFile}")

                    onSelectPicture(absolutePath.toString())
                    onSelectUri(uri)
                    onClosePicker()
                })
                CameraPicker(onSelect = { uri ->
                    onSelectUri(uri)
                    onClosePicker()
                })
            }
        }
    )
}
