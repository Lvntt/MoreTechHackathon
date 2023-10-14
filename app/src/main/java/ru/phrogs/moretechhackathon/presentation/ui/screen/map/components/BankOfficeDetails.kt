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
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.domain.entity.LoadType
import ru.phrogs.moretechhackathon.domain.entity.OpenHours
import ru.phrogs.moretechhackathon.domain.entity.OpenHoursElement
import ru.phrogs.moretechhackathon.presentation.ui.screen.common.AccentButton
import ru.phrogs.moretechhackathon.presentation.ui.theme.Blue
import ru.phrogs.moretechhackathon.presentation.ui.theme.DarkBlue
import ru.phrogs.moretechhackathon.presentation.ui.theme.Headline
import ru.phrogs.moretechhackathon.presentation.ui.theme.IconSize
import ru.phrogs.moretechhackathon.presentation.ui.theme.LabelAccent
import ru.phrogs.moretechhackathon.presentation.ui.theme.LabelRegular
import ru.phrogs.moretechhackathon.presentation.ui.theme.LightBlue
import ru.phrogs.moretechhackathon.presentation.ui.theme.WhiteBlue
import ru.phrogs.moretechhackathon.presentation.ui.theme.Padding16
import ru.phrogs.moretechhackathon.presentation.ui.theme.Padding20
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingLarge
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingSmall

// TODO add open hours for individuals
@Composable
fun BankOfficeDetails(
    distanceFromClient: Double,
    address: String,
    individualsLoad: LoadType,
    entitiesLoad: LoadType,
    openHours: OpenHours,
    openHoursIndividual: OpenHours,
    todayOpenHours: OpenHoursElement,
    availableForBlind: Boolean,
    metroStation: List<String>?,
    modifier: Modifier = Modifier,
    onRouteClick: () -> Unit = {}
) {
    var isIndividual by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .background(WhiteBlue)
            .padding(end = PaddingLarge, start = PaddingLarge, bottom = PaddingLarge)
            .safeContentPadding()
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
                style = ru.phrogs.moretechhackathon.presentation.ui.theme.Text,
                color = Blue
            )
        }

        Spacer(modifier = Modifier.height(PaddingSmall))

        Text(
            text = address,
            style = ru.phrogs.moretechhackathon.presentation.ui.theme.Text,
            color = Blue
        )

        Spacer(modifier = Modifier.height(Padding20))

        Divider(color = Blue)

        Spacer(modifier = Modifier.height(Padding20))

        SegmentedButton(
            items = listOf(
                stringResource(id = R.string.individuals),
                stringResource(id = R.string.entities)
            ),
            onItemSelection = {
                isIndividual = it == 0
            }
        )

        Spacer(modifier = Modifier.height(Padding20))

        when (isIndividual) {
            true -> BankOfficeContent(
                load = individualsLoad,
                openHours = openHoursIndividual,
                todayOpenHours = todayOpenHours,
                availableForBlind = availableForBlind,
                metroStation = metroStation,
                onRouteClick = onRouteClick
            )
            false -> BankOfficeContent(
                load = entitiesLoad,
                openHours = openHours,
                todayOpenHours = todayOpenHours,
                availableForBlind = availableForBlind,
                metroStation = metroStation,
                onRouteClick = onRouteClick
            )
        }
    }
}

@Composable
fun BankOfficeContent(
    load: LoadType,
    openHours: OpenHours,
    todayOpenHours: OpenHoursElement,
    availableForBlind: Boolean,
    metroStation: List<String>?,
    modifier: Modifier = Modifier,
    onRouteClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Padding20)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (load) {
                LoadType.LOW -> {
//                    Image(
//                        imageVector = ImageVector.vectorResource(id = R.drawable.low_load),
//                        modifier = Modifier.size(IconSize),
//                        contentDescription = null
//                    )
                    Spacer(modifier = Modifier.width(Padding16))
                    Row {
                        Text(
                            text = "${stringResource(id = R.string.load_now)} ",
                            style = LabelAccent,
                            color = DarkBlue
                        )
                        Text(
                            text = stringResource(id = R.string.low),
                            style = LabelRegular,
                            color = DarkBlue
                        )
                    }
                }
                LoadType.MEDIUM -> {
//                    Image(
//                        imageVector = ImageVector.vectorResource(id = R.drawable.medium_load),
//                        modifier = Modifier.size(IconSize),
//                        contentDescription = null
//                    )
                    Spacer(modifier = Modifier.width(Padding16))
                    Row {
                        Text(
                            text = "${stringResource(id = R.string.load_now)} ",
                            style = LabelAccent,
                            color = DarkBlue
                        )
                        Text(
                            text = stringResource(id = R.string.medium),
                            style = LabelRegular,
                            color = DarkBlue
                        )
                    }
                }
                LoadType.HIGH -> {
//                    Image(
//                        imageVector = ImageVector.vectorResource(id = R.drawable.high_load),
//                        modifier = Modifier.size(IconSize),
//                        contentDescription = null
//                    )
                    Spacer(modifier = Modifier.width(Padding16))
                    Row {
                        Text(
                            text = "${stringResource(id = R.string.load_now)} ",
                            style = LabelAccent,
                            color = DarkBlue
                        )
                        Text(
                            text = stringResource(id = R.string.high),
                            style = LabelRegular,
                            color = DarkBlue
                        )
                    }
                }
            }
        }

        Row {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.access_time),
                modifier = Modifier.size(IconSize),
                contentDescription = null,
                tint = DarkBlue
            )

            Spacer(modifier = Modifier.width(Padding16))

            Column {
                if (todayOpenHours.hours != stringResource(id = R.string.day_off)) {
                    Text(
                        text = "${stringResource(id = R.string.today_working_until)} ${todayOpenHours.hours.split("-")[1]}",
                        style = LabelAccent,
                        color = DarkBlue
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.day_off_today),
                        style = LabelAccent,
                        color = DarkBlue
                    )
                }

                for (openHoursElement in openHours.openHours) {
                    Text(
                        text = "${openHoursElement.days.capitalize(Locale.current)}: ${openHoursElement.hours.replace("-", " - ")}",
                        style = LabelRegular,
                        color = LightBlue
                    )
                }
            }
        }

        if (availableForBlind) {
            Row {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.accessible),
                    modifier = Modifier.size(IconSize),
                    contentDescription = null,
                    tint = DarkBlue
                )

                Spacer(modifier = Modifier.width(Padding16))

                Column {
                    Text(
                        text = stringResource(id = R.string.accessible_limited_mobility),
                        style = LabelAccent,
                        color = DarkBlue
                    )
                    Text(
                        text = stringResource(id = R.string.accessible_limited_mobility_desc),
                        style = LabelRegular,
                        color = DarkBlue
                    )
                }
            }
        }

        if (metroStation != null) {
            Row {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.subway),
                    modifier = Modifier.size(IconSize),
                    contentDescription = null,
                    tint = DarkBlue
                )

                Spacer(modifier = Modifier.width(Padding16))

                Column {
                    for (metro in metroStation) {
                        Text(
                            text = metro,
                            style = LabelAccent,
                            color = DarkBlue
                        )
                    }
                }
            }
        }

        AccentButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.make_a_route),
            onClick = onRouteClick
        )
    }
}

@Preview
@Composable
fun BankOfficeDetailsPreview() {
    BankOfficeDetails(
        distanceFromClient = 25.0,
        address = "119049, г. Москва, Ленинский пр-т, д. 11, стр. 1",
        individualsLoad = LoadType.LOW,
        entitiesLoad = LoadType.LOW,
        openHours = OpenHours(
            listOf(
                OpenHoursElement(
                    days = "пн",
                    hours = "09:00 - 18:00"
                ),
                OpenHoursElement(
                    days = "вт",
                    hours = "09:00 - 18:00"
                ),
                OpenHoursElement(
                    days = "ср",
                    hours = "09:00 - 18:00"
                ),
                OpenHoursElement(
                    days = "чт",
                    hours = "09:00 - 18:00"
                ),
                OpenHoursElement(
                    days = "пт",
                    hours = "09:00 - 18:00"
                ),
            )
        ),
        openHoursIndividual = OpenHours(
            listOf(
                OpenHoursElement(
                    days = "пн",
                    hours = "09:00 - 20:00"
                ),
                OpenHoursElement(
                    days = "вт",
                    hours = "09:00 - 20:00"
                ),
                OpenHoursElement(
                    days = "ср",
                    hours = "09:00 - 20:00"
                ),
                OpenHoursElement(
                    days = "чт",
                    hours = "09:00 - 20:00"
                ),
                OpenHoursElement(
                    days = "пт",
                    hours = "09:00 - 18:00"
                ),
            )
        ),
        todayOpenHours = OpenHoursElement(
            days = "пн",
            hours = "09:00 - 18:00"
        ),
        availableForBlind = true,
        metroStation = listOf("Октябрьская (Кольцевая линия)")
    )
}