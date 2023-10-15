package ru.phrogs.moretechhackathon.domain.entity

data class NfcService(
    val serviceCapability: CapabilityType,
    val serviceActivity: ActivityType
)