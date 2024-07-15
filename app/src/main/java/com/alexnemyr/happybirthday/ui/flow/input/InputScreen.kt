@file:OptIn(ExperimentalMaterial3Api::class)

package com.alexnemyr.happybirthday.ui.flow.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.alexnemyr.domain.util.formattedDate
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.navigation.Screen
import com.alexnemyr.happybirthday.ui.common.element.button.PrimaryButton
import com.alexnemyr.happybirthday.ui.common.element.button.SecondaryButton
import com.alexnemyr.happybirthday.ui.common.element.image.Avatar
import com.alexnemyr.happybirthday.ui.common.element.picker.PicturePicker
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Intent
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Label
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

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
            OutlinedTextField(
                value = state.name.toString(),
                onValueChange = { onAction(Intent.EditName(name = it)) },
                label = { Text(stringResource(R.string.input_name_label)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.date?.formattedDate?.isNotBlank() == true) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = state.date.formattedDate,
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W700)

                    )
                }

                SecondaryButton(
                    nameId = R.string.btn_chose_date,
                    onClick = { onAction(Intent.OnDatePicker(show = true)) }
                )

            }

            if (state.showDatePicker) {
                Date(
                    onDateSelected = { date -> onAction(Intent.EditDate(date = date.toString())) },
                    onDismiss = { onAction(Intent.OnDatePicker(show = false)) },
                )
            }

            PrimaryButton(
                nameId = R.string.btn_chose_picture,
                horizontalPadding = 0.dp,
                onClick = { onAction(Intent.OnPicturePicker(show = true)) },
            )

            Avatar(
                uri = state.uri,
                onShowPicturePicker = {}
            )

        }

        if (state.name?.isNotBlank() == true && state.date != 0.toString()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                PrimaryButton(
                    nameId = R.string.btn_show_birthday_screen,
                    onClick = { onAction(Intent.OnAnniversaryScreen) }
                )

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
