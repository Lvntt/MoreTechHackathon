package ru.phrogs.moretechhackathon.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.phrogs.moretechhackathon.domain.entity.DisabilityType
import ru.phrogs.moretechhackathon.presentation.uistate.onboarding.OnBoardingData
import ru.phrogs.moretechhackathon.presentation.uistate.onboarding.OnBoardingState
import java.util.UUID

class OnboardingViewModel : ViewModel() {

    val onBoardingState: State<OnBoardingState>
        get() = _onBoardingState
    private val _onBoardingState = mutableStateOf<OnBoardingState>(OnBoardingState.Initial)

    val onBoardingData: State<OnBoardingData>
        get() = _onBoardingData
    private val _onBoardingData = mutableStateOf<OnBoardingData>(
        OnBoardingData(
            entityIsIndividual = true, listOf(), listOf()
        )
    )

    val individualServices = listOf(
        Pair(UUID.randomUUID(), "Оформить дебетовую карту"),
        Pair(UUID.randomUUID(), "Оформить кредит"),
        Pair(UUID.randomUUID(), "Взять ипотеку"),
        Pair(UUID.randomUUID(), "Погасить кредит"),
        Pair(UUID.randomUUID(), "Совершить перевод"),
        Pair(UUID.randomUUID(), "Снять наличные")
    )

    val nonIndividualServices = listOf(
        Pair(UUID.randomUUID(), "Оформить бизнес-карту"),
        Pair(UUID.randomUUID(), "Оформить РКО"),
    )

    fun setEntityTypeContent() {
        _onBoardingState.value = OnBoardingState.EntitySelection
    }

    fun setServicesContent() {
        _onBoardingState.value = OnBoardingState.ServicesSelection
    }

    fun setDisabilityContent() {
        _onBoardingState.value = OnBoardingState.DisabilitySelection
    }

    fun saveOnBoardingData() {

    }

    fun setEntityType(index: Int) {
        if (index == 0) {
            _onBoardingData.value = _onBoardingData.value.copy(entityIsIndividual = true)
        } else if (index == 1) {
            _onBoardingData.value = _onBoardingData.value.copy(entityIsIndividual = false)
        }
    }

    fun setService(id: UUID, enabled: Boolean) {
        val uuidList = _onBoardingData.value.servicesList.toMutableList()
        if (enabled && uuidList.contains(id) || !enabled && !uuidList.contains(id)) {
            return
        }
        else if (enabled && !uuidList.contains(id)){
            uuidList.add(id)
        }
        else if (!enabled && uuidList.contains(id)) {
            uuidList.remove(id)
        }
        _onBoardingData.value = _onBoardingData.value.copy(servicesList = uuidList)
    }

    fun setDisability(disabilityType: DisabilityType, enabled: Boolean) {
        val disabilityList = _onBoardingData.value.disabilitiesList.toMutableList()
        if (enabled && disabilityList.contains(disabilityType) || !enabled && !disabilityList.contains(disabilityType)) {
            return
        }
        else if (enabled && !disabilityList.contains(disabilityType)){
            disabilityList.add(disabilityType)
        }
        else if (!enabled && disabilityList.contains(disabilityType)) {
            disabilityList.remove(disabilityType)
        }
        _onBoardingData.value = _onBoardingData.value.copy(disabilitiesList = disabilityList)
    }


}