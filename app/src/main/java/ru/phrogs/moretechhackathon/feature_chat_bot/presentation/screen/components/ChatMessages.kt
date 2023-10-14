package ru.phrogs.moretechhackathon.feature_chat_bot.presentation.screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.state.MessageType
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.state.Messages
import ru.phrogs.moretechhackathon.presentation.ui.theme.NavigationButtonBorderColor
import ru.phrogs.moretechhackathon.presentation.ui.theme.SystemBackgroundColor
import ru.phrogs.moretechhackathon.presentation.ui.theme.SystemMessageColor
import ru.phrogs.moretechhackathon.presentation.ui.theme.UserMessageBackgroundColor
import ru.phrogs.moretechhackathon.presentation.ui.theme.UserMessageColor


@Composable
fun ChatMessages(
    messages: Messages,
    modifier: Modifier
) {
    if (messages.messageType == MessageType.MESSAGE_TO_NAVIGATE) {
        NavigationalChatMessages(messages = messages, modifier = modifier)
    } else {
        RegularChatMessages(messages = messages, modifier = modifier)
    }
}

@Composable
fun RegularChatMessages(
    messages: Messages,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        for (message in messages.messages) {
            Row {
                if (messages.messageType == MessageType.SYSTEM_MESSAGE) {
                    Image(
                        painter = painterResource(id = R.drawable.vtb_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .width(35.dp)
                            .height(35.dp)
                            .align(Alignment.Bottom)
                            .padding(end = 8.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 20))
                        .background(
                            if (messages.messageType == MessageType.USER_MESSAGE) UserMessageBackgroundColor
                            else SystemBackgroundColor
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = message,
                        color = if (messages.messageType == MessageType.USER_MESSAGE) UserMessageColor
                        else SystemMessageColor,
                        modifier = Modifier.widthIn(max = 270.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NavigationalChatMessages(
    messages: Messages,
    modifier: Modifier
) {
    val systemMessages = if (messages.messages.size >= 3) {
        messages.messages.subList(0, 3)
    } else {
        messages.messages
    }

    Column(modifier = modifier) {
        for (message in systemMessages) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.vtb_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp)
                        .align(Alignment.Bottom)
                        .padding(end = 8.dp)
                )
                Button(
                    onClick = { Unit },
                    border = BorderStroke(2.dp, NavigationButtonBorderColor),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = message, color = SystemMessageColor)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
