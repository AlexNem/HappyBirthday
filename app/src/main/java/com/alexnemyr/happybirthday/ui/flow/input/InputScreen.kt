@file:OptIn(ExperimentalMaterial3Api::class)

package com.alexnemyr.happybirthday.ui.flow.input

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.alexnemyr.domain.util.formattedDate
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.navigation.Screen
import com.alexnemyr.happybirthday.ui.common.CameraPicker
import com.alexnemyr.happybirthday.ui.common.Photo
import com.alexnemyr.happybirthday.ui.common.PhotoPicker
import com.alexnemyr.happybirthday.ui.common.PickerBottomSheet
import com.alexnemyr.happybirthday.ui.common.buttonHeight
import com.alexnemyr.happybirthday.ui.common.util.fileFromContentUri
import com.alexnemyr.happybirthday.ui.common.util.getMediaFile
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Intent
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Label
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun InputScreen(
    inputViewModel: InputViewModel = koinViewModel(),
    navController: NavHostController
) {
    var showPhotoPicker by rememberSaveable { mutableStateOf(false) }
    val mviState by inputViewModel.states.collectAsStateWithLifecycle()

    inputViewModel.labels
        .onEach {
            when (it) {
                is Label.NavigateToAnniversary -> {
                    Timber.d("label -> onEach $it")
                    navController.navigate(Screen.AnniversaryBitmapWrapperScreen.name)
                }
            }
        }
        .collectAsState(null)

    Toolbar(
        content = { innerPadding ->
            InputContent(
                innerPadding = innerPadding,
                state = mviState,
                onAction = { inputViewModel.accept(it) },
                showPhotoPicker = { showPhotoPicker = true }
            )
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val processUri: (uri: Uri) -> Unit = { uri ->
                scope.launch {
                    delay(2000) // why delay?

                    // All Media.kt file functions should be distributed among viewmodels/repos/datasources OR some other factory classes.
                    // rememberCoroutineScope together with getMediaFile() will be canceled if current composable leaves composition.
                    val getMediaFile = getMediaFile(context, uri)
                    Timber.e("CameraPicker -> getFileName = $getMediaFile")
                }
            }

            if (showPhotoPicker) {
                PicturePicker(
                    // Should be handled via intents too
                    onClosePicker = { showPhotoPicker = false },
                    onSelectPicture = { path ->
                        inputViewModel.accept(Intent.EditPicture(uri = path))
                    },
                    onSelectUri = { uri ->
                        Timber.d("onSelectUri -> $uri")
                        processUri(uri)
                        inputViewModel.accept(Intent.EditPicture(uri = uri.toString()))
                    }
                )
            }
        }
    )
}

@Composable
fun PicturePicker(
    // These 3 callbacks can be united
    onClosePicker: () -> Unit,
    onSelectPicture: (path: String) -> Unit,
    onSelectUri: (uri: Uri) -> Unit
) {
    val context = LocalContext.current
    PickerBottomSheet(
        onDismiss = onClosePicker,
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

                    onSelectUri(uri)
                    onSelectPicture(uri.path ?: "")
                    onClosePicker()
                })
                CameraPicker(onSelect = { uri ->
                    uri.encodedPath?.let { onSelectPicture(it) }
                    onSelectUri(uri)
                    onClosePicker()
                })
            }
        }
    )
}

@Composable
fun InputContent(
    innerPadding: PaddingValues,
    state: InputStore.State,
    onAction: (Intent) -> Unit,
    // If we want to stick to MVI completely, we should keep showPhotoPicker handling in onAction callback too. (As well as showDialog var)
    showPhotoPicker: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            //name
            val label = "Name"

            OutlinedTextField(
                value = state.name ?: "",
                // BTW, saving to SharedPrefs with onValueChange is not the best idea in big projects
                onValueChange = { onAction(Intent.EditName(name = it)) },
                label = { Text(label) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            //date
            var showDialog by rememberSaveable { mutableStateOf(false) }

            ClickableText(text = AnnotatedString(text = "date = ${state.date?.formattedDate}"),
                style = TextStyle(
                    fontSize = 32.sp, fontWeight = FontWeight.W700
                ),
                onClick = { showDialog = true })
            Date(
                onDateSelected = { date -> onAction(Intent.EditDate(date = date.toString())) },
                onDismiss = { showDialog = false },
            )
            //picture
            Button(
                onClick = { showPhotoPicker() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight),
            ) { Text(text = "Picture") }

            Photo(state.uri, painterResource(id = R.drawable.ic_smile_fox), Modifier)
        }
        //next
        if (state.name?.isNotBlank() == true && state.date != "0") {
            Column(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Button(
                    onClick = { onAction(Intent.ShowAnniversaryScreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight)
                        .padding(horizontal = 16.dp),
                ) { Text(text = "Show birthday screen") }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun Toolbar(
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // You should try ktlint formatter
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                Text(stringResource(id = R.string.app_name))
            })
        },
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
private fun Date(
    onDateSelected: (date: Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    datePickerState.selectedDateMillis?.let {
        Timber.tag("Date ->").e("datePickerState $it")
        onDateSelected(it)
    }

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { onDismiss() },
                content = { Text("Ok") }
            )
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                content = { Text("Cancel") }
            )
        }
    ) {
        DatePicker(state = datePickerState)
    }
}