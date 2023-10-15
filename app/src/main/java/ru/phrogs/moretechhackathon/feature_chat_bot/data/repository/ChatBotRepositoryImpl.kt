package ru.phrogs.moretechhackathon.feature_chat_bot.data.repository

import kotlinx.coroutines.delay
import ru.phrogs.moretechhackathon.feature_chat_bot.data.remote.ChatBotApi
import ru.phrogs.moretechhackathon.feature_chat_bot.domain.repository.ChatBotRepository
import java.util.UUID

class ChatBotRepositoryImpl(
    private val chatBotApi: ChatBotApi
) : ChatBotRepository {
    override suspend fun getServices(message: String): List<Pair<UUID, String>> {
//        delay(1000)
//        return listOf(
//            Pair(UUID.randomUUID(), "Оформить дебетовую карту"),
//            Pair(UUID.randomUUID(), "Оформить кредит"),
//            Pair(UUID.randomUUID(), "Погасить кредит"),
//            Pair(UUID.randomUUID(), "Погасить кредит"),
//            Pair(UUID.randomUUID(), "Погасить кредит"),
//            Pair(UUID.randomUUID(), "Погасить кредит")
//        )
        return chatBotApi.getServices(message = message)
    }
}