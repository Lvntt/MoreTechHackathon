package ru.phrogs.moretechhackathon.feature_chat_bot.domain.repository

import java.util.UUID


interface ChatBotRepository {
    suspend fun getServices(message: String): List<Pair<UUID,String>>
}