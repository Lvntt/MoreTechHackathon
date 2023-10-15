package ru.phrogs.moretechhackathon.presentation.uistate.onboarding

sealed interface OnBoardingState {

    object Initial : OnBoardingState

    object EntitySelection : OnBoardingState

    object ServicesSelection : OnBoardingState

    object DisabilitySelection : OnBoardingState

}