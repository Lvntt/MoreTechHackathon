package ru.phrogs.moretechhackathon.domain.entity

data class Bank(
    val bankId: Int,
    val bankInfo: BankInfo,
    val distance: Double,
    val load: LoadType,
    val entityType: BankEntityType
)
