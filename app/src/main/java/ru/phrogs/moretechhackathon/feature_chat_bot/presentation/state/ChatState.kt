package ru.phrogs.moretechhackathon.feature_chat_bot.presentation.state

data class ChatState (
    val messages: List<Messages> = emptyList()
)