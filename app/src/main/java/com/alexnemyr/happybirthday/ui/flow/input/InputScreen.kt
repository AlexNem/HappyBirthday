@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.alexnemyr.happybirthday.ui.flow.input

import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.navigation.NavHostController
import com.alexnemyr.domain.util.TAG
import com.alexnemyr.happybirthday.ui.common.state.BirthdayState
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.navigation.Screen
import com.alexnemyr.happybirthday.ui.common.CameraPicker
import com.alexnemyr.happybirthday.ui.common.Photo
import com.alexnemyr.happybirthday.ui.common.PhotoPicker
import com.alexnemyr.happybirthday.ui.common.PickerBottomSheet
import com.alexnemyr.happybirthday.ui.common.buttonHeight
import com.alexnemyr.domain.util.formattedDate
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore
import timber.log.Timber

data class ErrorViewState(
    val show: Boolean,
    val message: String?
)

@Composable
fun InputScreen(
    viewModel: InputViewModel,
    navController: NavHostController
) {

    val showSheet = remember { mutableStateOf(false) }
    val errorState = remember { mutableStateOf(ErrorViewState(false, "")) }

    val mviState = viewModel.states.collectAsState(null).value

    val viewState: MutableState<BirthdayState?> = remember {
        mutableStateOf(null)
    }

    val label = viewModel.labels.collectAsState(null).value

    val context = LocalContext.current

    SideEffect {
        when (label) {
            is InputStore.Label.NavigateToAnniversary -> {
                navController.navigate(Screen.AnniversaryBitmapWrapperScreen.name)
            }

            else -> {}
        }
        if (errorState.value.show) {
            Toast.makeText(context, errorState.value.message ?: "Error ", Toast.LENGTH_LONG)
                .show()
        }
    }

    when (mviState) {
        is InputStore.State.Data -> {
            errorState.value = errorState.value.copy(show = false)

            viewState.value = BirthdayState(
                name = mviState.name,
                date = mviState.date,
                uriPath = mviState.uri
            )


        }

        is InputStore.State.Progress -> {
            errorState.value = errorState.value.copy(show = false)
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        is InputStore.State.Error -> {
            errorState.value = errorState.value.copy(show = true, message = mviState.message.message)
        }

        else -> {
            errorState.value = errorState.value.copy(show = false)
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
                    viewModel.accept(
                        InputStore.Intent.Edit(
                            userState.copy(name = name)
                        )
                    )
                },
                onDateChosen = { date ->
                    viewModel.accept(
                        InputStore.Intent.Edit(
                            userState.copy(date = date)
                        )
                    )
                },
                navTo = {
                    viewModel.accept(InputStore.Intent.ShowAnniversaryScreen)
                }
            )

            PicturePicker(
                showSheet = showSheet.value,
                onClosePicker = { showSheet.value = false },
                onSelectPicture = { path ->
                    viewModel.accept(InputStore.Intent.Edit(userState.copy(uriPath = path)))
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
    state: BirthdayState?,
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

            state?.let {

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