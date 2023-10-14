package ru.phrogs.moretechhackathon.feature_chat_bot.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.ChatViewModel

fun provideChatPresentationModule() = module {
    viewModel {
        ChatViewModel(get())
    }
}