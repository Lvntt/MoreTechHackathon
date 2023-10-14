package ru.phrogs.moretechhackathon.presentation.ui.screen.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import ru.phrogs.moretechhackathon.presentation.ui.theme.ButtonContentPadding
import ru.phrogs.moretechhackathon.presentation.ui.theme.ButtonRoundedCornerSize
import ru.phrogs.moretechhackathon.presentation.ui.theme.DarkBlue
import ru.phrogs.moretechhackathon.presentation.ui.theme.OnButtonTextStyle

@Composable
fun AccentButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = DarkBlue,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(ButtonRoundedCornerSize),
        contentPadding = PaddingValues(ButtonContentPadding)
    ) {
        Text(
            text = text,
            style = OnButtonTextStyle
        )
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
    }
}