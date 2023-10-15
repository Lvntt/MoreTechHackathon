package ru.phrogs.moretechhackathon.feature_visit_history.di

import org.koin.dsl.module
import ru.phrogs.moretechhackathon.feature_visit_history.data.local.HistoryStorage

fun provideHistoryDataModule() = module {
    single {
        HistoryStorage(get())
    }
}