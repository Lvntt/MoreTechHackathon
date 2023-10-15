package ru.phrogs.moretechhackathon.domain.entity

data class ATM(
    val id: Int,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val allDay: Boolean,
    val services: ATMServices
)
