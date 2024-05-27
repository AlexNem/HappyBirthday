package com.alexnemyr.happybirthday.ui.common.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.alexnemyr.domain.util.TAG
import timber.log.Timber

@Composable
fun Permission(
) {
//
//    val capturedImageUri = remember {
//        mutableStateOf<Uri?>(Uri.EMPTY)
//    }
//
//    val grantedState = remember { mutableStateOf(false) }
//
////    Text(text = "Permissions is Not Granted")
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestMultiplePermissions(),
//        onResult = { permissions ->
//            val permissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
//                acc && isPermissionGranted
//            }
//            Timber.tag(TAG).e("permissionLauncher -> $permissions")
//            if (!permissionsGranted) {
//                grantedState.value = false
//                Timber.tag(TAG).e("permissionLauncher -> isNotGranted")
//            }
//        }
//    )
//
//    val permissionsList: Array<String> =
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            arrayOf(
//
//                Manifest.permission.READ_MEDIA_IMAGES,
//            )
//        } else {
//            arrayOf(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//            )
//        }
//
//    if (!grantedState.value) {
//        SideEffect() {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                permissionLauncher.launch(permissionsList)
//            }
//        }
//    }

    val context = LocalContext.current
    val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Open camera
            Timber.tag(TAG).e("Permission -> isGranted")

        } else {
            // Show dialog
            Timber.tag(TAG).e("Permission -> isNotGranted")
        }
    }
    CheckAndRequestCameraPermission(
        context,
        permission,
        launcher
    )
}

@Composable
fun CheckAndRequestCameraPermission(
    context: Context,
    permission: String,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    val permissionCheckResult = ContextCompat.checkSelfPermission(context, permission)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        Timber.tag(TAG).e("Permission -> permissionCheckResult isGranted")
    } else {
        // Request a permission
        SideEffect {
            Timber.tag(TAG).e("Permission -> permissionCheckResult launch")
            launcher.launch(permission)
        }
    }
}

