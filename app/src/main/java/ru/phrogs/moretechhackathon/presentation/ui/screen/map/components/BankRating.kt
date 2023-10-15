package ru.phrogs.moretechhackathon.presentation.ui.screen.map.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.domain.entity.Bank
import ru.phrogs.moretechhackathon.domain.entity.BankEntity
import ru.phrogs.moretechhackathon.domain.entity.BankEntityType
import ru.phrogs.moretechhackathon.domain.entity.LoadType
import ru.phrogs.moretechhackathon.presentation.ui.theme.Blue
import ru.phrogs.moretechhackathon.presentation.ui.theme.CardColor
import ru.phrogs.moretechhackathon.presentation.ui.theme.CardElevationSize
import ru.phrogs.moretechhackathon.presentation.ui.theme.CardRoundedCornerSize
import ru.phrogs.moretechhackathon.presentation.ui.theme.DarkBlue
import ru.phrogs.moretechhackathon.presentation.ui.theme.IconSize
import ru.phrogs.moretechhackathon.presentation.ui.theme.LabelAccent
import ru.phrogs.moretechhackathon.presentation.ui.theme.LabelAccentSemiBold
import ru.phrogs.moretechhackathon.presentation.ui.theme.LabelRegular
import ru.phrogs.moretechhackathon.presentation.ui.theme.LightBlue
import ru.phrogs.moretechhackathon.presentation.ui.theme.LightBlueVariant
import ru.phrogs.moretechhackathon.presentation.ui.theme.Padding16
import ru.phrogs.moretechhackathon.presentation.ui.theme.Padding20
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingLarge
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingSmall
import ru.phrogs.moretechhackathon.presentation.ui.theme.Subtitle
import ru.phrogs.moretechhackathon.presentation.ui.theme.WhiteBlue

@Composable
fun BankRating(
    rating: List<Bank>,
    queryName: String?,
    onBankEntityClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(WhiteBlue)
            .padding(PaddingLarge)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Padding20)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.bank_offices),
            style = Subtitle,
            color = LightBlue,
            textAlign = TextAlign.Center
        )

        Text(
            text = if (queryName == null)
                "${stringResource(id = R.string.rating_desc)}."
            else "${stringResource(id = R.string.rating_desc)} ${stringResource(id = R.string.for_service)} $queryName.",
            style = ru.phrogs.moretechhackathon.presentation.ui.theme.Text,
            color = LightBlueVariant
        )

        for (bankEntity in rating) {
            Card(
                shape = RoundedCornerShape(CardRoundedCornerSize),
                colors = CardDefaults.cardColors(
                    containerColor = CardColor
                ),
                modifier = Modifier
                    .clickable {
                        onBankEntityClick(bankEntity.bankId)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = CardElevationSize),
            ) {
                Column(
                    modifier = Modifier.padding(Padding16)
                ) {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.account_balance),
                            modifier = Modifier.size(IconSize),
                            contentDescription = null,
                            tint = DarkBlue
                        )

                        Spacer(modifier = Modifier.width(Padding16))

                        Column {
                            if (bankEntity.entityType == BankEntityType.OFFICE) {
                                Text(
                                    text = stringResource(id = R.string.vtb_office),
                                    style = LabelAccent,
                                    color = DarkBlue
                                )
                            } else {
                                Text(
                                    text = stringResource(id = R.string.vtb_atm),
                                    style = LabelAccent,
                                    color = DarkBlue
                                )
                            }

                            Text(
                                text = bankEntity.bankInfo.address,
                                style = LabelAccent,
                                color = DarkBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(PaddingSmall))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.social_distance),
                            modifier = Modifier.size(IconSize),
                            contentDescription = null,
                            tint = Blue
                        )

                        Spacer(modifier = Modifier.width(Padding16))

                        var distance = bankEntity.distance.toString()
                        if (bankEntity.distance % 1.0 == 0.0) {
                            distance = bankEntity.distance.toInt().toString()
                        }

                        Text(
                            text = "$distance ${stringResource(id = R.string.km_away)}",
                            style = LabelRegular,
                            color = Blue
                        )
                    }

                    Spacer(modifier = Modifier.height(PaddingSmall))

                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (bankEntity.load) {
                            LoadType.LOW -> {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.low_load),
                                    modifier = Modifier.size(IconSize),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(Padding16))
                                Row {
                                    Text(
                                        text = "${stringResource(id = R.string.load_now)} ",
                                        style = LabelAccent,
                                        color = Blue
                                    )
                                    Text(
                                        text = stringResource(id = R.string.low),
                                        style = LabelRegular,
                                        color = Blue
                                    )
                                }
                            }
                            LoadType.MEDIUM -> {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.medium_load),
                                    modifier = Modifier.size(IconSize),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(Padding16))
                                Row {
                                    Text(
                                        text = "${stringResource(id = R.string.load_now)} ",
                                        style = LabelAccent,
                                        color = Blue
                                    )
                                    Text(
                                        text = stringResource(id = R.string.medium),
                                        style = LabelRegular,
                                        color = Blue
                                    )
                                }
                            }
                            LoadType.HIGH -> {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.high_load),
                                    modifier = Modifier.size(IconSize),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(Padding16))
                                Row {
                                    Text(
                                        text = "${stringResource(id = R.string.load_now)} ",
                                        style = LabelAccent,
                                        color = Blue
                                    )
                                    Text(
                                        text = stringResource(id = R.string.high),
                                        style = LabelRegular,
                                        color = Blue
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(PaddingSmall))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.more_info),
                            style = LabelAccentSemiBold,
                            color = DarkBlue
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.keyboard_arrow_right),
                            modifier = Modifier.size(IconSize),
                            contentDescription = null
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Padding20))
    }
}