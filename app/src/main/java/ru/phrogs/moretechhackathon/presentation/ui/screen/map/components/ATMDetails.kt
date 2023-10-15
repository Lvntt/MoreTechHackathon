package ru.phrogs.moretechhackathon.presentation.ui.screen.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.domain.entity.ATM
import ru.phrogs.moretechhackathon.domain.entity.ATMServices
import ru.phrogs.moretechhackathon.domain.entity.ActivityType
import ru.phrogs.moretechhackathon.domain.entity.BlindService
import ru.phrogs.moretechhackathon.domain.entity.CapabilityType
import ru.phrogs.moretechhackathon.domain.entity.ChargeRubSupportService
import ru.phrogs.moretechhackathon.domain.entity.EurSupportService
import ru.phrogs.moretechhackathon.domain.entity.NfcService
import ru.phrogs.moretechhackathon.domain.entity.QrService
import ru.phrogs.moretechhackathon.domain.entity.RubSupportService
import ru.phrogs.moretechhackathon.domain.entity.UsdSupportService
import ru.phrogs.moretechhackathon.domain.entity.WheelchairService
import ru.phrogs.moretechhackathon.presentation.ui.screen.common.AccentButton
import ru.phrogs.moretechhackathon.presentation.ui.theme.Blue
import ru.phrogs.moretechhackathon.presentation.ui.theme.DarkBlue
import ru.phrogs.moretechhackathon.presentation.ui.theme.Error
import ru.phrogs.moretechhackathon.presentation.ui.theme.Headline
import ru.phrogs.moretechhackathon.presentation.ui.theme.IconSize
import ru.phrogs.moretechhackathon.presentation.ui.theme.LabelAccent
import ru.phrogs.moretechhackathon.presentation.ui.theme.LabelRegular
import ru.phrogs.moretechhackathon.presentation.ui.theme.Padding16
import ru.phrogs.moretechhackathon.presentation.ui.theme.Padding20
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingLarge
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingSmall
import ru.phrogs.moretechhackathon.presentation.ui.theme.Text
import ru.phrogs.moretechhackathon.presentation.ui.theme.WhiteBlue

@Composable
fun ATMDetails(
    atm: ATM,
    distanceFromClient: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(WhiteBlue)
            .padding(PaddingLarge)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.bank_office),
                style = Headline,
                color = DarkBlue
            )

            Text(
                text = "$distanceFromClient " + stringResource(id = R.string.km_away),
                style = Text,
                color = Blue
            )
        }

        Spacer(modifier = Modifier.height(PaddingSmall))

        Text(
            text = atm.address,
            style = Text,
            color = Blue
        )

        Spacer(modifier = Modifier.height(Padding20))

        Divider(color = Blue)

        Spacer(modifier = Modifier.height(Padding20))

        // TODO

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Padding20)
        ) {
            if (atm.allDay) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.access_time),
                        modifier = Modifier.size(IconSize),
                        contentDescription = null,
                        tint = DarkBlue
                    )

                    Spacer(modifier = Modifier.width(Padding16))

                    Column {
                        Text(
                            text = stringResource(id = R.string.works_around_the_clock),
                            style = LabelAccent,
                            color = DarkBlue
                        )
                        Text(
                            text = stringResource(id = R.string.works_around_the_clock_desc),
                            style = LabelRegular,
                            color = DarkBlue
                        )
                    }

                    Spacer(modifier = Modifier.width(Padding16))
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.accessible),
                    modifier = Modifier.size(IconSize),
                    contentDescription = null,
                    tint = DarkBlue
                )

                Spacer(modifier = Modifier.width(Padding16))

                when (atm.services.wheelchair.serviceCapability) {
                    CapabilityType.SUPPORTED -> {
                        Column {
                            Text(
                                text = stringResource(id = R.string.accessible_limited_mobility),
                                style = LabelAccent,
                                color = DarkBlue
                            )
                            when (atm.services.wheelchair.serviceActivity) {
                                ActivityType.AVAILABLE -> Unit
                                ActivityType.UNAVAILABLE -> {
                                    Text(
                                        text = stringResource(id = R.string.currently_unavailable),
                                        style = LabelRegular,
                                        color = Error
                                    )
                                }
                                ActivityType.UNKNOWN -> Unit
                            }
                        }
                    }
                    CapabilityType.UNSUPPORTED -> {
                        Text(
                            text = stringResource(id = R.string.not_accessible_limited_mobility),
                            style = LabelAccent,
                            color = DarkBlue
                        )
                    }
                    CapabilityType.UNKNOWN -> Unit
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.blind),
                    modifier = Modifier.size(IconSize),
                    contentDescription = null,
                    tint = DarkBlue
                )

                Spacer(modifier = Modifier.width(Padding16))

                when (atm.services.blind.serviceCapability) {
                    CapabilityType.SUPPORTED -> {
                        Column {
                            Text(
                                text = stringResource(id = R.string.accessible_blind),
                                style = LabelAccent,
                                color = DarkBlue
                            )
                            when (atm.services.blind.serviceActivity) {
                                ActivityType.AVAILABLE -> Unit
                                ActivityType.UNAVAILABLE -> {
                                    Text(
                                        text = stringResource(id = R.string.currently_unavailable),
                                        style = LabelRegular,
                                        color = Error
                                    )
                                }
                                ActivityType.UNKNOWN -> Unit
                            }
                        }
                    }
                    CapabilityType.UNSUPPORTED -> {
                        Text(
                            text = stringResource(id = R.string.not_accessible_blind),
                            style = LabelAccent,
                            color = DarkBlue
                        )
                    }
                    CapabilityType.UNKNOWN -> Unit
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.nfc),
                    modifier = Modifier.size(IconSize),
                    contentDescription = null,
                    tint = DarkBlue
                )

                Spacer(modifier = Modifier.width(Padding16))

                when (atm.services.nfcForBankCards.serviceCapability) {
                    CapabilityType.SUPPORTED -> {
                        Column {
                            Text(
                                text = stringResource(id = R.string.nfc_works),
                                style = LabelAccent,
                                color = DarkBlue
                            )
                            when (atm.services.nfcForBankCards.serviceActivity) {
                                ActivityType.AVAILABLE -> Unit
                                ActivityType.UNAVAILABLE -> {
                                    Text(
                                        text = stringResource(id = R.string.currently_unavailable),
                                        style = LabelRegular,
                                        color = Error
                                    )
                                }
                                ActivityType.UNKNOWN -> Unit
                            }
                        }
                    }
                    CapabilityType.UNSUPPORTED -> {
                        Text(
                            text = stringResource(id = R.string.nfc_does_not_work),
                            style = LabelAccent,
                            color = DarkBlue
                        )
                    }
                    CapabilityType.UNKNOWN -> Unit
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.qr_code),
                    modifier = Modifier.size(IconSize),
                    contentDescription = null,
                    tint = DarkBlue
                )

                Spacer(modifier = Modifier.width(Padding16))

                when (atm.services.qrRead.serviceCapability) {
                    CapabilityType.SUPPORTED -> {
                        Column {
                            Text(
                                text = stringResource(id = R.string.qr_works),
                                style = LabelAccent,
                                color = DarkBlue
                            )
                            when (atm.services.qrRead.serviceActivity) {
                                ActivityType.AVAILABLE -> Unit
                                ActivityType.UNAVAILABLE -> {
                                    Text(
                                        text = stringResource(id = R.string.currently_unavailable),
                                        style = LabelRegular,
                                        color = Error
                                    )
                                }
                                ActivityType.UNKNOWN -> Unit
                            }
                        }
                    }
                    CapabilityType.UNSUPPORTED -> {
                        Text(
                            text = stringResource(id = R.string.qr_does_not_work),
                            style = LabelAccent,
                            color = DarkBlue
                        )
                    }
                    CapabilityType.UNKNOWN -> Unit
                }
            }

            AccentButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.make_a_route),
                onClick = { /*TODO*/ }
            )
        }
    }
}

@Preview
@Composable
fun ATMDetailsPreview() {
    ATMDetails(
        atm = ATM(
            id = 1,
            address = "ул. Богородский Вал, д. 6, корп. 1",
            latitude = 55.802432,
            longitude = 37.704547,
            allDay = true,
            services = ATMServices(
                WheelchairService(CapabilityType.SUPPORTED, ActivityType.UNAVAILABLE),
                BlindService(CapabilityType.SUPPORTED, ActivityType.AVAILABLE),
                NfcService(CapabilityType.SUPPORTED, ActivityType.AVAILABLE),
                QrService(CapabilityType.SUPPORTED, ActivityType.UNAVAILABLE),
                UsdSupportService(CapabilityType.SUPPORTED, ActivityType.AVAILABLE),
                ChargeRubSupportService(CapabilityType.SUPPORTED, ActivityType.AVAILABLE),
                EurSupportService(CapabilityType.SUPPORTED, ActivityType.AVAILABLE),
                RubSupportService(CapabilityType.SUPPORTED, ActivityType.AVAILABLE)
            )
        ),
        distanceFromClient = 25.2f
    )
}