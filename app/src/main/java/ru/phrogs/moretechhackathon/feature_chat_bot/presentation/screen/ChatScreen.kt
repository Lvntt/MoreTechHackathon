package ru.phrogs.moretechhackathon.feature_chat_bot.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.screen.components.ChatMessages
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.state.ChatState
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.state.MessageType
import ru.phrogs.moretechhackathon.ui.theme.SystemBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    state: ChatState,
    onSendMessage: (String) -> Unit,
    onDisconnect: () -> Unit,
) {
    val message = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        IconButton(onClick = onDisconnect, modifier = Modifier.align(Alignment.Start)) {
            Icon(
                painter = painterResource(id = R.drawable.keyboard_backspace),
                contentDescription = null,
                modifier = Modifier
                    .width(28.dp)
                    .height(22.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            reverseLayout = true
        ) {
            items(state.messages) { message ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ChatMessages(
                        messages = message,
                        modifier = Modifier
                            .align(
                                if (message.messageType == MessageType.USER_MESSAGE) Alignment.CenterEnd else Alignment.CenterStart
                            )
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message.value,
                onValueChange = { message.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Сообщение")
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onSendMessage(message.value)
                            message.value = ""
                        },
                        enabled = message.value.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null
                        )
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledPlaceholderColor = SystemBackgroundColor,
                    disabledTrailingIconColor = SystemBackgroundColor,
                    containerColor = SystemBackgroundColor
                ),
                shape = RoundedCornerShape(percent = 15)
            )
        }
    }
}