package ru.phrogs.moretechhackathon.domain.usecase

import ru.phrogs.moretechhackathon.domain.entity.BankCoordinates
import ru.phrogs.moretechhackathon.domain.repository.BankRepository

class GetAllBankCoordinatesUseCase(private val bankRepository: BankRepository) {

    suspend operator fun invoke(): List<BankCoordinates> = bankRepository.getAllBankCoordinates()

}