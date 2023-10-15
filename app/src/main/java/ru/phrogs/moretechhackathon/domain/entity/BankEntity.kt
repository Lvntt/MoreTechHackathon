package ru.phrogs.moretechhackathon.domain.entity

data class BankEntity(
    val entityId: Int,
    val entityType: BankEntityType,
    val address: String,
    val distanceFromClient: Float,
    val load: LoadType
)
