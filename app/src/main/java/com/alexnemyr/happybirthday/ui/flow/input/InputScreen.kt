@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.alexnemyr.happybirthday.ui.flow.input

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.alexnemyr.domain.util.TAG
import com.alexnemyr.domain.util.formattedDate
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.navigation.Screen
import com.alexnemyr.happybirthday.ui.common.CameraPicker
import com.alexnemyr.happybirthday.ui.common.Photo
import com.alexnemyr.happybirthday.ui.common.PhotoPicker
import com.alexnemyr.happybirthday.ui.common.PickerBottomSheet
import com.alexnemyr.happybirthday.ui.common.buttonHeight
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Intent
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Label
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.State
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@Composable
fun InputScreen(
    viewModel: InputViewModel,
    navController: NavHostController
) {
    val showSheet = remember { mutableStateOf(false) }
    val mviState = viewModel.states.collectAsState(null).value
    val viewState: MutableState<State?> = remember { mutableStateOf(null) }

    viewModel.labels
        .onEach {
            when (it) {
                is Label.NavigateToAnniversary -> {
                    Timber.tag(TAG).d("label -> onEach $it")
                    navController.navigate(Screen.AnniversaryBitmapWrapperScreen.name)
                }
            }
        }
        .collectAsState(null).value

    when (mviState) {
        is State -> {
            viewState.value = mviState
        }

//        is InputStore.State.Progress -> {
//            errorState.value = errorState.value.copy(show = false)
//            Box(modifier = Modifier.fillMaxSize()) {
//                CircularProgressIndicator(Modifier.align(Alignment.Center))
//            }
//        }
//
//        is InputStore.State.Error -> {
//            errorState.value = errorState.value.copy(show = true, message = mviState.message.message)
//        }

        else -> {
            Timber.tag(TAG).d("mviState is else $mviState")
        }
    }

    Toolbar(content = { innerPadding ->
        viewState.value?.let { userState ->
            InputContent(
                innerPadding = innerPadding,
                showSheet = showSheet,
                state = viewState.value,
                onEditName = { name ->
                    viewModel.accept(Intent.EditName(name = name))
                },
                onDateChosen = { date ->
                    viewModel.accept(Intent.EditDate(date = date))
                },
                navTo = {
                    viewModel.accept(Intent.ShowAnniversaryScreen)
                }
            )

            PicturePicker(
                showSheet = showSheet.value,
                onClosePicker = { showSheet.value = false },
                onSelectPicture = { path ->
                    viewModel.accept(Intent.EditPicture(uri = path))
                }
            )
        }


    })

}

@Composable
fun PicturePicker(
    showSheet: Boolean,
    onClosePicker: () -> Unit,
    onSelectPicture: (path: String) -> Unit
) {
    if (showSheet) {
        PickerBottomSheet(
            onDismiss = onClosePicker,
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 32.dp)
                ) {
                    PhotoPicker(onSelect = { uri ->
                        uri.encodedPath?.let { onSelectPicture(it) }
                        onClosePicker()
                    })
                    CameraPicker(onSelect = { uri ->
                        uri.encodedPath?.let { onSelectPicture(it) }
                        onClosePicker()
                    })
                }
            })
    }
}

@Composable
fun InputContent(
    innerPadding: PaddingValues,
    showSheet: MutableState<Boolean>,
    state: State?,
    onEditName: (name: String) -> Unit,
    onDateChosen: (date: String) -> Unit,
    navTo: () -> Unit
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
                value = state?.name ?: "",
                onValueChange = { onEditName(it) },
                label = { Text(label) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            //date
            val showDialog = rememberSaveable { mutableStateOf(false) }

            ClickableText(text = AnnotatedString(text = "date = ${state?.date?.formattedDate}"),
                style = TextStyle(
                    fontSize = 32.sp, fontWeight = FontWeight.W700
                ),
                onClick = {
                    showDialog.value = true
                })
            Date(
                onOk = { date -> onDateChosen(date.toString()) },
                showDialog = showDialog
            )
            //picture
            Button(
                onClick = { showSheet.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight),
            ) { Text(text = "Picture") }

            state?.uri?.let {
                Photo(it, painterResource(id = R.drawable.ic_smile_fox), Modifier)
            }

        }
        //next
        if (state?.name?.isNotBlank() == true && state.date != "0") {
            Column(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Button(
                    onClick = navTo,
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
    onOk: (date: Long?) -> Unit,
    showDialog: MutableState<Boolean>
) {
    val datePickerState = rememberDatePickerState()

    datePickerState.selectedDateMillis?.let {
        Timber.tag("Date ->").e("datePickerState $it")
        onOk(it)
    }

    if (showDialog.value) {
        DatePickerDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}