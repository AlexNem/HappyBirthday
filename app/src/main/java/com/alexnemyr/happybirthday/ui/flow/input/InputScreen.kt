@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.alexnemyr.happybirthday.ui.common.MVI_TAG
import com.alexnemyr.happybirthday.ui.common.Photo
import com.alexnemyr.happybirthday.ui.common.PicturePicker
import com.alexnemyr.happybirthday.ui.common.buttonHeight
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Intent
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Label
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@Composable
fun InputScreen(
    viewModel: InputViewModel,
    navController: NavHostController
) {
    val state by viewModel.states.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = viewModel.labels) {
        viewModel.labels
            .onEach {
                when (it) {
                    is Label.NavigateToAnniversary -> {
                        Timber.tag(MVI_TAG).d("label -> onEach $it")
                        navController.navigate(Screen.AnniversaryBitmapWrapperScreen.name)
                    }
                }
            }.collect()
    }

    Toolbar(
        content = { innerPadding ->
            InputContent(
                innerPadding = innerPadding,
                state = state,
                onAction = { viewModel.accept(it) }
            )

            if (state.showPicturePicker) {
                PicturePicker(
                    onClosePicker = { viewModel.accept(Intent.OnPicturePicker(show = false)) },
                    onSelectPicture = { path -> viewModel.accept(Intent.EditPicture(uri = path)) },
                    onSelectUri = { uri -> viewModel.accept(Intent.EditPicture(uri = uri.toString())) }
                )
            }
        }
    )
}

@Composable
fun InputContent(
    innerPadding: PaddingValues,
    state: InputStore.State,
    onAction: (Intent) -> Unit,
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

            ClickableText(text = AnnotatedString(text = "date = ${state.date?.formattedDate}"),
                style = TextStyle(
                    fontSize = 32.sp, fontWeight = FontWeight.W700
                ),
                onClick = { onAction(Intent.OnDatePicker(show = true)) }
            )
            if (state.showDatePicker) {
                Date(
                    onDateSelected = { date -> onAction(Intent.EditDate(date = date.toString())) },
                    onDismiss = { onAction(Intent.OnDatePicker(show = false)) },
                )
            }
            //picture
            Button(
                onClick = { onAction(Intent.OnPicturePicker(show = true)) },
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
                    onClick = { onAction(Intent.OnAnniversaryScreen) },
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
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(id = R.string.app_name))
                }
            )
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
