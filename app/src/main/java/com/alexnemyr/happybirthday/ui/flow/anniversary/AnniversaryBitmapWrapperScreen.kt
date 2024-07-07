package com.alexnemyr.happybirthday.ui.flow.anniversary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.ui.common.element.button.PrimaryButton
import com.alexnemyr.happybirthday.ui.common.util.getAnniversaryResources
import com.alexnemyr.happybirthday.ui.common.util.screenshotWrapper
import com.alexnemyr.happybirthday.ui.common.util.share

@Composable
fun AnniversaryBitmapWrapperScreen(
    viewModel: AnniversaryViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val res by remember { mutableStateOf(getAnniversaryResources()) }

    Box {
        val bitmapWrapper = screenshotWrapper(
            content = {
                AnniversaryScreen(
                    viewModel = viewModel,
                    navController = navController,
                    res = res
                )
            }
        )

        PrimaryButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            nameId = R.string.btn_share_the_new,
            colorId = res.btnColor,
            horizontalPadding = 16.dp,
            onClick = {
                share(bitmapWrapper.invoke(), context)
            }
        )

    }

}


