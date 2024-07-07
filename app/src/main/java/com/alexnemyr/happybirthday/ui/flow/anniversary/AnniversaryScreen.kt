package com.alexnemyr.happybirthday.ui.flow.anniversary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.alexnemyr.domain.util.Age
import com.alexnemyr.domain.util.age
import com.alexnemyr.domain.util.toDate
import com.alexnemyr.domain.util.yearOrMonthTitle
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.navigation.Screen
import com.alexnemyr.happybirthday.ui.common.element.image.Avatar
import com.alexnemyr.happybirthday.ui.common.element.picker.PicturePicker
import com.alexnemyr.happybirthday.ui.common.util.AnniversaryResources
import com.alexnemyr.happybirthday.ui.common.util.NumberIcon
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Intent
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Label
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@Composable
fun AnniversaryScreen(
    viewModel: AnniversaryViewModel,
    navController: NavHostController,
    res: AnniversaryResources
) {
    val state by viewModel.states.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = viewModel.labels) {
        viewModel.accept(Intent.FetchUser)
        viewModel.labels.onEach {
            when (it) {
                is Label.NavigateToInput -> {
                    navController.navigate(Screen.InputScreen.name)
                }
            }
        }.collect()
    }

    AnniversaryContent(
        state = state,
        res = res,
        onBackNav = { viewModel.accept(Intent.OnInputScreen) },
        onShowPicturePicker = { show -> viewModel.accept(Intent.OnPicturePicker(show)) },
    )
    if (state.showPicturePicker)
        PicturePicker(
            onClosePicker = { Intent.OnPicturePicker(show = false) },
            onSelectPicture = { path -> viewModel.accept(Intent.EditPicture(uri = path)) },
            onSelectUri = { }
        )
}

@Composable
fun AnniversaryContent(
    state: State,
    res: AnniversaryResources,
    onBackNav: () -> Unit,
    onShowPicturePicker: (value: Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .background(colorResource(id = res.backgroundColor))
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

        Avatar(
            resources = res,
            uri = state.uri,
            isClickable = true,
            onShowPicturePicker = { value -> onShowPicturePicker(value) }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = res.backgroundDrawable),
                    contentScale = ContentScale.FillBounds
                )

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val name = state.name?.uppercase()
                val subTitle: String by lazy {
                    val age = state.date?.age
                    if (age == null) {
                        return@lazy "0"
                    } else {
                        return@lazy age.yearOrMonthTitle
                    }
                }

                Spacer(modifier = Modifier.height(56.dp))

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

                state.date?.let { AgeComponent(it) }

                Spacer(modifier = Modifier.height(14.dp))

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
fun AgeComponent(date: String) {
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

        Numbers(age = toDate(date))

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

