package ru.phrogs.moretechhackathon.domain.entity

data class ATMServices(
    val wheelchair: WheelchairService,
    val blind: BlindService,
    val nfcForBankCards: NfcService,
    val qrRead: QrService,
    val supportsUsd: UsdSupportService,
    val supportsChargeRub: ChargeRubSupportService,
    val supportsEur: EurSupportService,
    val supportsRub: RubSupportService
)