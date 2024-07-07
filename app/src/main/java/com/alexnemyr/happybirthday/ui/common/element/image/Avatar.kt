package com.alexnemyr.happybirthday.ui.common.element.image

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alexnemyr.happybirthday.ui.common.util.AnniversaryResources
import com.alexnemyr.happybirthday.ui.common.util.BGType
import com.alexnemyr.happybirthday.ui.common.util.resources

@Composable
fun Avatar(
    resources: AnniversaryResources = BGType.PELICAN.resources,
    uri: String?,
    isClickable: Boolean = false,
    onShowPicturePicker: (Boolean) -> Unit
) {
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
                        border = BorderStroke(8.dp, colorResource(id = resources.btnColor)),
                        shape = CircleShape
                    )
                    .background(colorResource(id = resources.btnBGColor), CircleShape)
                    .size(300.dp)
            )

            Photo(uri, painterResource(id = resources.btnIcon), Modifier.align(Alignment.Center))

            if (isClickable) {
                val padding = 20.dp
                val size = 54
                IconButton(
                    onClick = { onShowPicturePicker(true) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size((size + padding.value).dp)
                        .padding(top = padding, end = padding)
                ) {
                    Image(
                        modifier = Modifier
                            .size(54.dp),
                        painter = painterResource(id = resources.btnAddIcon),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }


        }

    }

}
