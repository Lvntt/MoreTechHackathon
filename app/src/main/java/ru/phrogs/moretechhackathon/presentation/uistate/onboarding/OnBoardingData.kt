package ru.phrogs.moretechhackathon.presentation.uistate.onboarding

import ru.phrogs.moretechhackathon.domain.entity.DisabilityType
import java.util.UUID

data class OnBoardingData(
    val entityIsIndividual: Boolean,
    val servicesList: List<UUID>,
    val disabilitiesList: List<DisabilityType>
)
