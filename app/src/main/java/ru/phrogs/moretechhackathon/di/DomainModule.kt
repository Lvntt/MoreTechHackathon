package ru.phrogs.moretechhackathon.di

import org.koin.dsl.module
import ru.phrogs.moretechhackathon.data.repository.BankRepositoryImpl
import ru.phrogs.moretechhackathon.domain.repository.BankRepository
import ru.phrogs.moretechhackathon.domain.usecase.GetAllBankCoordinatesUseCase
import ru.phrogs.moretechhackathon.domain.usecase.GetBankInfoUseCase

private fun provideBankRepository(): BankRepository = BankRepositoryImpl()

fun provideDomainModule() = module {
    single {
        provideBankRepository()
    }

    factory {
        GetAllBankCoordinatesUseCase(get())
    }

    factory {
        GetBankInfoUseCase(get())
    }
}