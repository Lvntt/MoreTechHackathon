package ru.phrogs.moretechhackathon.feature_chat_bot.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import ru.phrogs.moretechhackathon.feature_chat_bot.data.repository.ChatBotRepositoryImpl
import ru.phrogs.moretechhackathon.feature_chat_bot.domain.usecase.GetServicesUseCase
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.state.ChatState
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.state.Messages
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.state.MessageType

class ChatViewModel(
//    private val getServicesUseCase: GetServicesUseCase
) : ViewModel() {
    private val _state = mutableStateOf(ChatState())
    val state: State<ChatState> = _state

    init {
        addMessages(listOf(FIRST_SYSTEM_MESSAGE, SECOND_SYSTEM_MESSAGE), MessageType.SYSTEM_MESSAGE)
    }

    private companion object {
        const val FIRST_SYSTEM_MESSAGE =
            "В этом чате вы можете указать интересующую услугу по ключевым словам, и мы подберем для вас наиболее подходящие отделения ВТБ. Например: кредит."
        const val SECOND_SYSTEM_MESSAGE =
            "Вы также можете перейти на экран выбора услуг и конкретизировать цель визита в отделение."
    }

    private fun addMessages(messages: List<String>, type: MessageType) {
        _state.value = _state.value.copy(
            messages = listOf(Messages(messages, type)) + _state.value.messages
        )
    }

    fun sendMessage(message: String) {

        val nonEmptyLines = message.lines().filter { it.isNotBlank() }
        val resultString = nonEmptyLines.joinToString("\n")

        val getServicesUseCase = GetServicesUseCase(ChatBotRepositoryImpl())

        addMessages(listOf(resultString), MessageType.USER_MESSAGE)

        viewModelScope.launch {
            try {
                val answer = getServicesUseCase(message)
                addMessages(answer.map { it.second }, MessageType.MESSAGE_TO_NAVIGATE)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                addMessages(listOf(e.message.toString()), MessageType.SYSTEM_MESSAGE)
            }
        }
    }

    fun backFromChat() {
        Unit
    }
}