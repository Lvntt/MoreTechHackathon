package ru.phrogs.moretechhackathon.feature_chat_bot.data.remote

import java.util.UUID

interface ChatBotApi {
    suspend fun getServices(message: String): List<Pair<UUID, String>>
}