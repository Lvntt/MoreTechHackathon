package ru.phrogs.moretechhackathon.domain.entity

data class BankInfo(
    val salePointName: String,
    val address: String,
    val salePointCode: String?,
    val status: String?,
    val openHours: OpenHours,
    val openHoursIndividual: OpenHours,
    val hasRamp: Boolean,
    val latitude: Float,
    val longitude: Float,
    val metroStation: String?
)