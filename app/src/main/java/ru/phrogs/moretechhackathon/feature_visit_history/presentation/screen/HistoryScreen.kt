package ru.phrogs.moretechhackathon.feature_visit_history.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.screen.components.ChatMessages
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.state.MessageType
import ru.phrogs.moretechhackathon.feature_visit_history.presentation.HistoryViewModel
import ru.phrogs.moretechhackathon.feature_visit_history.presentation.screen.components.HistoryItemInformation
import ru.phrogs.moretechhackathon.presentation.ui.theme.Headline
import ru.phrogs.moretechhackathon.presentation.ui.theme.Title

@Composable
fun HistoryScreen() {

    val viewModel: HistoryViewModel = koinViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "История поиска",
                style = Title,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = { Unit },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.keyboard_backspace),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }

        if (viewModel.state.value.historyItems.isEmpty()) {
            Text(
                text = "История поиска отделения по услугам или банкоматам пуста.",
                style = ru.phrogs.moretechhackathon.presentation.ui.theme.Text,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(40.dp),
        ) {
            items(viewModel.state.value.historyItems) { historyItem ->
                HistoryItemInformation(historyItem = historyItem)
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
}