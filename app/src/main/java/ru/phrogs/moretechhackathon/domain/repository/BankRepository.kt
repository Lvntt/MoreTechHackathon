package ru.phrogs.moretechhackathon.domain.repository

import ru.phrogs.moretechhackathon.domain.entity.BankCoordinates

interface BankRepository {

    suspend fun getAllBankCoordinates(): List<BankCoordinates>

}