package ru.phrogs.moretechhackathon.domain.usecase

import ru.phrogs.moretechhackathon.domain.repository.BankRepository

class GetBankInfoUseCase(private val bankRepository: BankRepository) {

    suspend operator fun invoke(bankId: Int) = bankRepository.getBankInfoById(bankId)

}