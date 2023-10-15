package ru.phrogs.moretechhackathon.feature_chat_bot.presentation.state

data class Messages(
    val messages: List<String>,
    val messageType: MessageType,
)
