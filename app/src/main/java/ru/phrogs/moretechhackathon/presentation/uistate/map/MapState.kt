package ru.phrogs.moretechhackathon.presentation.uistate.map

import ru.phrogs.moretechhackathon.domain.entity.BankCoordinates

sealed interface MapState {

    object Error : MapState

    object Loading : MapState

    data class Content(
        val bankCoordinates: List<BankCoordinates>
    ) : MapState

}