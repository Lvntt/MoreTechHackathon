package ru.phrogs.moretechhackathon.domain.repository

import ru.phrogs.moretechhackathon.domain.entity.BankCoordinates
import ru.phrogs.moretechhackathon.domain.entity.BankInfo

interface BankRepository {

    suspend fun getAllBankCoordinates(): List<BankCoordinates>

    suspend fun getBankInfoById(bankId: Int): BankInfo

}