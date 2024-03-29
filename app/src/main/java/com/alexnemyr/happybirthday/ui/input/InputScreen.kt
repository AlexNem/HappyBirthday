@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.alexnemyr.happybirthday.ui.input

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.TAG
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun InputScreen() {

    val mviViewModel = koinViewModel<InputViewModel>()

    val state = mviViewModel.state.collectAsState()


    val showSheet = remember { mutableStateOf(false) }

    val capturedImageUri = remember { mviViewModel.mutableState.value.capturedImageUri }
    val name = remember { mviViewModel.mutableState.value.name }
    val date = remember { mviViewModel.mutableState.value.date }

    SideEffect {
        mviViewModel.saveInfo(
            state.value.name.value,
            state.value.date.value.toString(),
            state.value.capturedImageUri.value.toString(),
        )
    }

    Timber.tag(TAG).e(
        "InputPreview -> " +
                "\nname = ${name.value}" +
                "\ndate = ${date.value}" +
                "\ncapturedImageUri = ${capturedImageUri.value}" +
                "\n" +
                "\nstate = ${state.value}"
    )

    if (showSheet.value) {
        PickerBottomSheet(onDismiss = { showSheet.value = false }, content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                PhotoPicker(onSelect = { uri ->
                    capturedImageUri.value = uri
                    showSheet.value = false
                })
                CameraPicker(onSelect = { uri ->
                    capturedImageUri.value = uri
                    showSheet.value = false
                })
            }
        })
    }

    Toolbar { innerPadding ->
        InputDetails(innerPadding, showSheet, InputState(capturedImageUri, name, date))
    }
}

@Composable
fun InputDetails(
    innerPadding: PaddingValues,
    showSheet: MutableState<Boolean>,
    state: InputState
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
                value = state.name.value,
                onValueChange = { state.name.value = it },
                label = { Text(label) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            //date
            val showDialog = rememberSaveable { mutableStateOf(false) }
            val onOk: (date: Long?) -> Unit = {
                it?.let {
                    state.date.value = it
                }
            }

            ClickableText(text = AnnotatedString(text = "date = ${state.date.value}"),
                style = TextStyle(
                    fontSize = 32.sp, fontWeight = FontWeight.W700
                ),
                onClick = {
                    showDialog.value = true
                })
            Date(
                onOk = { date -> onOk(date) }, showDialog = showDialog
            )
            //picture
            Button(
                onClick = { showSheet.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight),
            ) { Text(text = "Picture") }

            if (state.capturedImageUri.value.path?.isNotEmpty() == true) {
                AsyncImage(
                    model = state.capturedImageUri.value,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .size(photoSize),
                    contentScale = ContentScale.Crop
                )
                Timber.tag(TAG)
                    .i("isNotEmpty() == true// capturedImageUri = ${state.capturedImageUri.value}")

            } else {
                Image(
                    modifier = Modifier
                        .background(Color.Transparent, RoundedCornerShape(100))
                        .size(photoSize),
                    painter = painterResource(id = R.drawable.empty_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }

        }
        //next
        if (state.name.value.isNotBlank() && state.date.value != 0L) {
            val context = LocalContext.current
            Column(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, "Show birthday screen", Toast.LENGTH_SHORT).show()
                    }, //todo
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
    onOk: (date: Long?) -> Unit, showDialog: MutableState<Boolean>
) {
    val datePickerState = rememberDatePickerState()
    if (showDialog.value) {
        DatePickerDialog(onDismissRequest = { showDialog.value = false }, confirmButton = {
            TextButton(onClick = {
                onOk(datePickerState.selectedDateMillis)
                showDialog.value = false
            }) {
                Text("Ok")
            }
        }, dismissButton = {
            TextButton(onClick = { showDialog.value = false }) {
                Text("Cancel")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }
}