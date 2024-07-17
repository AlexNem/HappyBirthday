package com.alexnemyr.happybirthday.ui.common.element.button

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.ui.common.buttonHeight
import com.alexnemyr.happybirthday.ui.theme.AppTheme

@Preview
@Composable
private fun PrimaryButtonPreview() {
    PrimaryButton(
        nameId = R.string.btn_show_birthday_screen
    ) {}
}

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    @StringRes nameId: Int,
    color: Color = MaterialTheme.colorScheme.primary, //colorResource(id =  R.color.btn_pelican),
    fontWeight: FontWeight = FontWeight.Bold,
    fontSize: TextUnit = 18.sp,
    horizontalPadding: Dp = 16.dp,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .padding(horizontal = horizontalPadding),
    ) {
        Text(
            text = stringResource(id = nameId).uppercase(),
            style = TextStyle(
                fontWeight = fontWeight,
                fontSize = fontSize
            )
        )
    }
}
