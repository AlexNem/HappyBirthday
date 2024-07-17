package com.alexnemyr.happybirthday.ui.common.element.button

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexnemyr.happybirthday.R
import com.alexnemyr.happybirthday.ui.common.secondaryButtonHeight

@Preview
@Composable
private fun SecondaryButtonPreview() {
    SecondaryButton(
        nameId = R.string.btn_show_birthday_screen
    ) {}
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    @StringRes nameId: Int,
    color: Color = MaterialTheme.colorScheme.primary,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    horizontalPadding: Dp = 0.dp,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .wrapContentWidth()
            .height(secondaryButtonHeight)
            .padding(horizontal = horizontalPadding),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(
            text = stringResource(id = nameId).uppercase(),
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = fontWeight
            )
        )
    }
}
