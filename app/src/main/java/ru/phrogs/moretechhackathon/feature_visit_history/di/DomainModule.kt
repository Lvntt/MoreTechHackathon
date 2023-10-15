package ru.phrogs.moretechhackathon.feature_visit_history.di

import org.koin.dsl.module
import ru.phrogs.moretechhackathon.feature_visit_history.data.local.HistoryStorage
import ru.phrogs.moretechhackathon.feature_visit_history.data.repository.HistoryRepositoryImpl
import ru.phrogs.moretechhackathon.feature_visit_history.domain.repository.HistoryRepository
import ru.phrogs.moretechhackathon.feature_visit_history.domain.usecase.GetHistoryUseCase

private fun provideHistoryRepository(historyStorage: HistoryStorage): HistoryRepository =
    HistoryRepositoryImpl(historyStorage)

fun provideHistoryDomainModule() = module {

    single {
        provideHistoryRepository(get())
    }

    factory {
        GetHistoryUseCase(get())
    }
}