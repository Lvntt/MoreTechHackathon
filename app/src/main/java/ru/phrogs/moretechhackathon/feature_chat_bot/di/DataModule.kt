package ru.phrogs.moretechhackathon.feature_chat_bot.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.phrogs.moretechhackathon.feature_chat_bot.data.remote.ChatBotApi


private fun provideChatBotApi(): ChatBotApi {
    val retrofit = Retrofit.Builder()
        .baseUrl(ChatBotApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(ChatBotApi::class.java)
}

fun provideChatDataModule() = module {
    single {
        provideChatBotApi()
    }
}