package ru.phrogs.moretechhackathon.presentation.ui.screen.map.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.presentation.ui.theme.DarkBlue
import ru.phrogs.moretechhackathon.presentation.ui.theme.LabelAccentSemiBold
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingLarge
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingMedium
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingSmall
import ru.phrogs.moretechhackathon.presentation.ui.theme.TopBarTextStyle

@Composable
fun NavigationOverlay(
    address: String,
    timeMinutes: Int,
    onBackPress: () -> Unit
) {
    BackHandler {
        onBackPress()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkBlue)
                .padding(PaddingSmall)
                .statusBarsPadding(), contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = onBackPress) {
                Icon(
                    painter = painterResource(id = R.drawable.keyboard_backspace_icon),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.route),
                style = TopBarTextStyle,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(topStart = PaddingMedium, topEnd = PaddingMedium)
                )
                .background(DarkBlue)
                .padding(start = PaddingLarge, end = PaddingLarge, bottom = PaddingMedium)
                .navigationBarsPadding()
                , contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Row(modifier = Modifier.padding(vertical = PaddingMedium)) {
                    Icon(
                        painter = painterResource(id = R.drawable.route_car_icon),
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(PaddingSmall))
                    Text(
                        text = stringResource(id = R.string.drive),
                        style = LabelAccentSemiBold,
                        color = Color.White,
                    )
                }
                Text(
                    text = stringResource(id = R.string.where, address),
                    style = ru.phrogs.moretechhackathon.presentation.ui.theme.Text,
                    color = Color.White
                )
                Text(
                    text = stringResource(id = R.string.route_time, timeMinutes),
                    style = ru.phrogs.moretechhackathon.presentation.ui.theme.Text,
                    color = Color.White
                )
            }
        }
    }
}