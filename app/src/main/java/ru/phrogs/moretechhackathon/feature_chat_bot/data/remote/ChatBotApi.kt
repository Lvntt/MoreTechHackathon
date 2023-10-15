package ru.phrogs.moretechhackathon.feature_chat_bot.data.remote

import java.util.UUID
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatBotApi {
    @GET("chat-bot")
    suspend fun getServices(@Query("query") message: String): List<Pair<UUID, String>>

    companion object {
        const val BASE_URL = "http://localhost:8080/"
    }
}