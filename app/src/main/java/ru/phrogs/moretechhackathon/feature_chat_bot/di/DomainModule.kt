package ru.phrogs.moretechhackathon.feature_chat_bot.di

import org.koin.dsl.module
import ru.phrogs.moretechhackathon.feature_chat_bot.data.remote.ChatBotApi
import ru.phrogs.moretechhackathon.feature_chat_bot.data.repository.ChatBotRepositoryImpl
import ru.phrogs.moretechhackathon.feature_chat_bot.domain.repository.ChatBotRepository
import ru.phrogs.moretechhackathon.feature_chat_bot.domain.usecase.GetServicesUseCase

private fun provideChatBotRepository(chatBotApi: ChatBotApi): ChatBotRepository = ChatBotRepositoryImpl(chatBotApi = chatBotApi)

fun provideChatDomainModule() = module {

    single {
        provideChatBotRepository(get())
    }

    factory {
        GetServicesUseCase(get())
    }
}