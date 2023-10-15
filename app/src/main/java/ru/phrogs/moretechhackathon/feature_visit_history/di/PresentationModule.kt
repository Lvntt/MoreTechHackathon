package ru.phrogs.moretechhackathon.feature_visit_history.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.phrogs.moretechhackathon.feature_visit_history.presentation.HistoryViewModel

fun provideHistoryPresentationModule() = module {
    viewModel {
        HistoryViewModel(get())
    }
}