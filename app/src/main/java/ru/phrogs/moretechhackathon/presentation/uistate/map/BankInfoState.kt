package ru.phrogs.moretechhackathon.presentation.uistate.map

import ru.phrogs.moretechhackathon.domain.entity.BankInfo

sealed interface BankInfoState {

    object Loading : BankInfoState

    object Error : BankInfoState

    data class Content(val bankId: Int, val bankInfo: BankInfo, val distance: Double) : BankInfoState

}