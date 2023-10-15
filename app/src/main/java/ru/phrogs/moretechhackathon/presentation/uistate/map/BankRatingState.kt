package ru.phrogs.moretechhackathon.presentation.uistate.map

import ru.phrogs.moretechhackathon.domain.entity.Bank

sealed interface BankRatingState {

    object Loading : BankRatingState

    object Error : BankRatingState

    data class Content(val rating: List<Bank>) : BankRatingState

}