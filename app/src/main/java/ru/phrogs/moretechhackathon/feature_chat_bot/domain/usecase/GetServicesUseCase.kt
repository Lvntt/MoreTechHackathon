package ru.phrogs.moretechhackathon.feature_chat_bot.domain.usecase

import ru.phrogs.moretechhackathon.feature_chat_bot.domain.repository.ChatBotRepository
import java.util.UUID

class GetServicesUseCase(private val repository: ChatBotRepository) {
    suspend operator fun invoke(message: String): List<Pair<UUID,String>> {
        return repository.getServices(message = message)
    }
}