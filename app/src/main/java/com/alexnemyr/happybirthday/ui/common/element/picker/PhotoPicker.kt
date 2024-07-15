package com.alexnemyr.happybirthday.ui.common.element.picker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexnemyr.domain.util.TAG
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.ui.common.element.button.PrimaryButton
import timber.log.Timber

@Composable
fun PhotoPicker(
    onSelect: (uri: Uri) -> Unit
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            Timber.tag(TAG).d("PhotoPicker -> uri = $uri")
            onSelect(uri ?: Uri.EMPTY)
        }
    )

    fun launchPhotoPicker() {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryButton(
            nameId = R.string.btn_select_a_photo,
            horizontalPadding = 0.dp,
            onClick = { launchPhotoPicker() }
        )
    }
}
