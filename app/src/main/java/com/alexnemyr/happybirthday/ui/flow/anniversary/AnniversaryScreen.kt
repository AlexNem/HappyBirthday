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
import androidx.compose.runtime.MutableState
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
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.BirthdayViewModel
import com.alexnemyr.happybirthday.ui.common.Age
import com.alexnemyr.happybirthday.ui.common.AnniversaryRes
import com.alexnemyr.happybirthday.ui.common.BGType
import com.alexnemyr.happybirthday.ui.common.BirthdayState
import com.alexnemyr.happybirthday.ui.common.CameraPicker
import com.alexnemyr.happybirthday.ui.common.NumberIcon
import com.alexnemyr.happybirthday.ui.common.Photo
import com.alexnemyr.happybirthday.ui.common.PhotoPicker
import com.alexnemyr.happybirthday.ui.common.PickerBottomSheet
import com.alexnemyr.happybirthday.ui.common.age
import com.alexnemyr.happybirthday.ui.common.getYearOrMonth

@Composable
fun AnniversaryScreen(
    viewModel: BirthdayViewModel
) {

    val showSheet = remember { mutableStateOf(false) }

    val capturedImageUri = remember { viewModel.mutableState.value.capturedImageUri }
    val name = remember { viewModel.mutableState.value.name }
    val date = remember { viewModel.mutableState.value.date }

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


    Anniversary(
        showSheet,
        BirthdayState(capturedImageUri, name, date),
        viewModel,
    )

}

@Composable
fun Anniversary(
    showSheet: MutableState<Boolean>,
    state: BirthdayState,
    viewModel: BirthdayViewModel,
) {

    val bgList = listOf(BGType.FOX, BGType.PELICAN)
    val bg: AnniversaryRes = when (bgList.random()) {
        BGType.FOX -> AnniversaryRes(
            color = colorResource(id = R.color.bg_fox),
            painter = painterResource(id = R.drawable.bg_fox),
            bntColor = colorResource(id = R.color.btn_fox),
            bntBGColor = colorResource(id = R.color.btn_bg_fox),
            bntIcon = painterResource(id = R.drawable.ic_smile_fox),
            bntAddIcon = painterResource(id = R.drawable.ic_add_fox),
        )

        BGType.PELICAN -> AnniversaryRes(
            color = colorResource(id = R.color.bg_pelican),
            painter = painterResource(id = R.drawable.bg_pelican),
            bntColor = colorResource(id = R.color.btn_pelican),
            bntBGColor = colorResource(id = R.color.btn_bg_pelican),
            bntIcon = painterResource(id = R.drawable.ic_smile_pelican),
            bntAddIcon = painterResource(id = R.drawable.ic_add_pelican),
        )
    }

    Box(
        modifier = Modifier
            .background(bg.color)
            .fillMaxSize()
    ) {

        IconButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart),
            onClick = { viewModel.navTo(true) }) {
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
                        .border(BorderStroke(8.dp, bg.bntColor), shape = CircleShape)
                        .background(bg.bntBGColor, CircleShape)
                        .size(300.dp)
                )
                Photo(state, bg.bntIcon, Modifier.align(Alignment.Center))

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
                        painter = bg.bntAddIcon,
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
                    painter = bg.painter,
                    contentScale = ContentScale.FillBounds
                )

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(56.dp))
                val name = state.name.value.uppercase()
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
                    Numbers(age = viewModel.toDate(state.date.value))
                    Spacer(modifier = Modifier.width(22.dp))
                    Image(
                        modifier = Modifier
                            .wrapContentHeight(),
                        painter = painterResource(id = R.drawable.right_swirls),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                val subTitle = state.date.value.getYearOrMonth(state.date.value.age)
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
fun Numbers(age: Age) {
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

