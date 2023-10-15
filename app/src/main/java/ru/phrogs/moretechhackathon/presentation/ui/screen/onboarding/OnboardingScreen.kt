package ru.phrogs.moretechhackathon.presentation.ui.screen.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.domain.entity.DisabilityType
import ru.phrogs.moretechhackathon.presentation.ui.navigation.MoreTechDestinations
import ru.phrogs.moretechhackathon.presentation.ui.screen.common.AccentButton
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.SegmentedButton
import ru.phrogs.moretechhackathon.presentation.ui.theme.Blue
import ru.phrogs.moretechhackathon.presentation.ui.theme.Headline
import ru.phrogs.moretechhackathon.presentation.ui.theme.Padding20
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingLarge
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingMedium
import ru.phrogs.moretechhackathon.presentation.ui.theme.Title
import ru.phrogs.moretechhackathon.presentation.ui.theme.UserMessageBackgroundColor
import ru.phrogs.moretechhackathon.presentation.ui.theme.VeryLightBlue
import ru.phrogs.moretechhackathon.presentation.uistate.onboarding.OnBoardingState
import ru.phrogs.moretechhackathon.presentation.viewmodel.OnboardingViewModel
import java.util.UUID

@Composable
fun OnboardingScreen(
    navController: NavController
) {
    val onboardingViewModel: OnboardingViewModel = koinViewModel()
    val state by remember { onboardingViewModel.onBoardingState }
    val data by remember { onboardingViewModel.onBoardingData }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeContentPadding(),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(targetState = state, label = "") {
            when (it) {
                OnBoardingState.DisabilitySelection -> DisabilitySelectionContent(
                    setDisabilityActive = onboardingViewModel::setDisability,
                    activeDisabilities = data.disabilitiesList,
                    nextScreenCallback = {
                        if (!(data.disabilitiesList.isEmpty() || data.disabilitiesList.contains(
                                DisabilityType.NONE
                            ) && data.disabilitiesList.size > 1)
                        ) {
                            onboardingViewModel.saveOnBoardingData()
                            navController.navigate(MoreTechDestinations.MAIN)
                        }
                    })

                OnBoardingState.EntitySelection -> EntityTypeContent(
                    onboardingViewModel::setEntityType, onboardingViewModel::setServicesContent
                )

                OnBoardingState.Initial -> OnboardingContent(nextScreenCallback = onboardingViewModel::setEntityTypeContent,
                    skipToMapCallback = { navController.navigate(MoreTechDestinations.MAIN) })

                OnBoardingState.ServicesSelection -> ServicesSelectionContent(serviceTypes = if (data.entityIsIndividual) onboardingViewModel.individualServices else onboardingViewModel.nonIndividualServices,
                    setServiceActive = onboardingViewModel::setService,
                    activeServices = data.servicesList,
                    nextScreenCallback = {
                        if (data.servicesList.isNotEmpty()) onboardingViewModel.setDisabilityContent()
                    })
            }
        }
    }
}

@Composable
private fun EntityTypeContent(
    onItemSelection: (Int) -> Unit, nextScreenCallback: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight()
            .padding(bottom = PaddingLarge * 1.5f, top = PaddingLarge * 4),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.clientTypeQuestion),
            color = Color.Black,
            style = Title
        )
        SegmentedButton(
            items = listOf(
                stringResource(id = R.string.individual), stringResource(id = R.string.legal)
            ), onItemSelection = onItemSelection
        )
        AccentButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.continueButtonText),
            onClick = nextScreenCallback,
            containerColor = Blue,
            contentColor = Color.White,
            textStyle = Headline,
            icon = ImageVector.vectorResource(id = R.drawable.keyboard_arrow_right)
        )
    }
}

@Composable
private fun DisabilitySelectionContent(
    setDisabilityActive: (DisabilityType, Boolean) -> Unit,
    activeDisabilities: List<DisabilityType>,
    nextScreenCallback: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight()
            .padding(bottom = PaddingLarge * 1.5f, top = PaddingLarge * 4),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.disabilityTitle), color = Color.Black, style = Title
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(
                PaddingMedium
            )
        ) {
            AccentButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.visionProblems),
                onClick = {
                    setDisabilityActive(
                        DisabilityType.VISION, !activeDisabilities.contains(DisabilityType.VISION)
                    )
                },
                containerColor = if (activeDisabilities.contains(DisabilityType.VISION)) UserMessageBackgroundColor else VeryLightBlue,
                contentColor = if (activeDisabilities.contains(DisabilityType.VISION)) Color.White else Blue,
                textStyle = Headline
            )

            AccentButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.movementProblems),
                onClick = {
                    setDisabilityActive(
                        DisabilityType.MOVEMENT,
                        !activeDisabilities.contains(DisabilityType.MOVEMENT)
                    )
                },
                containerColor = if (activeDisabilities.contains(DisabilityType.MOVEMENT)) UserMessageBackgroundColor else VeryLightBlue,
                contentColor = if (activeDisabilities.contains(DisabilityType.MOVEMENT)) Color.White else Blue,
                textStyle = Headline
            )

            AccentButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.noDisability),
                onClick = {
                    setDisabilityActive(
                        DisabilityType.NONE, !activeDisabilities.contains(DisabilityType.NONE)
                    )
                },
                containerColor = if (activeDisabilities.contains(DisabilityType.NONE)) UserMessageBackgroundColor else VeryLightBlue,
                contentColor = if (activeDisabilities.contains(DisabilityType.NONE)) Color.White else Blue,
                textStyle = Headline
            )
        }


        AccentButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.continueButtonText),
            onClick = nextScreenCallback,
            containerColor = Blue,
            contentColor = Color.White,
            textStyle = Headline,
            icon = ImageVector.vectorResource(id = R.drawable.keyboard_arrow_right)
        )
    }
}

@Composable
private fun ServicesSelectionContent(
    serviceTypes: List<Pair<UUID, String>>,
    setServiceActive: (UUID, Boolean) -> Unit,
    activeServices: List<UUID>,
    nextScreenCallback: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight()
            .padding(bottom = PaddingLarge * 1.5f, top = PaddingLarge * 4),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.servicesSelectionTitle),
            color = Color.Black,
            style = Title
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
                    PaddingMedium
                )
            ) {
                items(count = serviceTypes.size) {
                    AccentButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = serviceTypes[it].second,
                        onClick = {
                            setServiceActive(
                                serviceTypes[it].first,
                                !activeServices.contains(serviceTypes[it].first)
                            )
                        },
                        containerColor = if (activeServices.contains(serviceTypes[it].first)) UserMessageBackgroundColor else VeryLightBlue,
                        contentColor = if (activeServices.contains(serviceTypes[it].first)) Color.White else Blue,
                        textStyle = Headline
                    )
                }
            }
        }

        AccentButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.continueButtonText),
            onClick = nextScreenCallback,
            containerColor = Blue,
            contentColor = Color.White,
            textStyle = Headline,
            icon = ImageVector.vectorResource(id = R.drawable.keyboard_arrow_right)
        )
    }
}

@Composable
private fun OnboardingContent(
    nextScreenCallback: () -> Unit, skipToMapCallback: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth(0.85f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            Padding20
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.vtb_logo_splash), contentDescription = null
        )
        AccentButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.selectServices),
            onClick = nextScreenCallback,
            containerColor = Blue,
            contentColor = Color.White,
            textStyle = Headline
        )
        AccentButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.viewBanks),
            onClick = skipToMapCallback,
            containerColor = VeryLightBlue,
            contentColor = Blue,
            textStyle = Headline
        )
    }
}