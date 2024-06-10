package com.alexnemyr.happybirthday.ui.flow.anniversary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.alexnemyr.domain.util.Age
import com.alexnemyr.domain.util.TAG
import com.alexnemyr.domain.util.age
import com.alexnemyr.domain.util.toDate
import com.alexnemyr.domain.util.yearOrMonthTitle
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.navigation.Screen
import com.alexnemyr.happybirthday.ui.common.Photo
import com.alexnemyr.happybirthday.ui.common.util.NumberIcon
import com.alexnemyr.happybirthday.ui.common.util.getAnniversaryResources
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Intent
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Label
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.State
import com.alexnemyr.happybirthday.ui.flow.input.PicturePicker
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


@Composable
fun AnniversaryScreen(
    viewModel: AnniversaryViewModel,
    navController: NavHostController,
) {

    val mviState = viewModel.states.collectAsState(null).value
    val showSheet = remember { mutableStateOf(false) }
    val viewState: MutableState<State?> = remember { mutableStateOf(null) }


    LaunchedEffect(key1 = mviState) {
        viewModel.accept(Intent.FetchUser)
    }

    viewModel.labels.onEach {
        Timber.tag(TAG).d("label -> onEach $it")
        when (it) {
            is Label.NavigateToInput -> {
                navController.navigate(Screen.InputScreen.name)
            }
        }
    }.collectAsState(initial = null)

    when (mviState) {
        is State -> {
            viewState.value = mviState
            viewState.value?.let { state ->
                AnniversaryContent(
                    showSheet = showSheet,
                    state = state,
                    onBackNav = { viewModel.accept(Intent.ShowInputScreen) }
                )
                // TODO hoist PicturePicker showing state
                PicturePicker(
                    onClosePicker = { showSheet.value = false },
                    onSelectPicture = { path ->
                        viewModel.accept(Intent.EditPicture(uri = path))
                    },
                    onSelectUri = {  }
                )
            }

        }

//        is AnniversaryStore.State.Progress -> {
//            Box(modifier = Modifier.fillMaxSize()) {
//                CircularProgressIndicator(Modifier.align(Alignment.Center))
//            }
//        }
//
//        is AnniversaryStore.State.Error -> {
//            Toast.makeText(LocalContext.current, "Error", Toast.LENGTH_LONG).show()
//        }

        else -> {
            Timber.tag(TAG).d("mviState is else $mviState")
        }
    }

}

@Composable
fun AnniversaryContent(
    showSheet: MutableState<Boolean>,
    state: State,
    onBackNav: () -> Unit
) {

    val bg = remember { mutableStateOf(getAnniversaryResources()) }.value

    Box(
        modifier = Modifier
            .background(colorResource(id = bg.backgroundColor))
            .fillMaxSize()
    ) {

        IconButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart),
            onClick = onBackNav
        ) {
            Icon(painterResource(id = R.drawable.ic_navigate), contentDescription = null)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 192.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(300.dp)
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            border = BorderStroke(8.dp, colorResource(id = bg.btnColor)),
                            shape = CircleShape
                        )
                        .background(colorResource(id = bg.btnBGColor), CircleShape)
                        .size(300.dp)
                )
                Photo(state.uri, painterResource(id = bg.btnIcon), Modifier.align(Alignment.Center))

                val padding = 20.dp
                IconButton(
                    onClick = { showSheet.value = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size((54 + padding.value).dp)
                        .padding(top = padding, end = padding)
                ) {
                    Image(
                        modifier = Modifier
                            .size(54.dp),
                        painter = painterResource(id = bg.btnAddIcon),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }

            }

        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = bg.backgroundDrawable),
                    contentScale = ContentScale.FillBounds
                )

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(56.dp))
                val name = state.name?.uppercase()
                Text(
                    text = "TODAY $name IS",
                    style = TextStyle(
                        fontSize = 21.sp,
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .padding(horizontal = 100.dp)
                        .height(60.dp)
                )
                Spacer(modifier = Modifier.height(13.dp))
                AgeComponent(state.date)
                Spacer(modifier = Modifier.height(14.dp))

                var subTitle = ""
                state.date?.age?.let {
                    subTitle = it.yearOrMonthTitle
                }
                Text(
                    text = "$subTitle OLD ",
                    style = TextStyle(
                        fontSize = 21.sp,
                        fontWeight = FontWeight.W500
                    )
                )
            }
        }
    }
}

@Composable
fun AgeComponent(date: String?) {
    date?.let {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .wrapContentHeight(),
                painter = painterResource(id = R.drawable.left_swirls),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(22.dp))
            Numbers(age = toDate(it))
            Spacer(modifier = Modifier.width(22.dp))
            Image(
                modifier = Modifier
                    .wrapContentHeight(),
                painter = painterResource(id = R.drawable.right_swirls),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }

}


@Composable
fun Numbers(age: Age?) {
    age?.let {
        if (age.year == 0) {
            age.month.toString().forEach { ageInt ->
                val numberList = NumberIcon.list.find { it.number == ageInt.digitToInt() }
                Image(
                    modifier = Modifier
                        .height(100.dp),
                    painter = painterResource(id = numberList?.iconId ?: R.drawable.number_zero),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        } else {
            age.year.toString().forEach { ageInt ->
                val numberList = NumberIcon.list.find { it.number == ageInt.digitToInt() }
                Image(
                    modifier = Modifier
                        .height(100.dp),
                    painter = painterResource(id = numberList?.iconId ?: R.drawable.number_zero),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        }
    }
}

