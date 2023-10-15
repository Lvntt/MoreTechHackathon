package ru.phrogs.moretechhackathon.feature_visit_history.presentation.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.feature_visit_history.domain.model.HistoryItem
import ru.phrogs.moretechhackathon.presentation.ui.theme.AdditionalText
import ru.phrogs.moretechhackathon.presentation.ui.theme.HistoryDateColor
import ru.phrogs.moretechhackathon.presentation.ui.theme.HistoryTextColor
import ru.phrogs.moretechhackathon.presentation.ui.theme.LabelAccentSemiBold

@Composable
fun HistoryItemInformation(historyItem: HistoryItem) {
    when (historyItem) {
        is HistoryItem.BankBranch -> BankBranchInformation(historyItem)
        is HistoryItem.CashMachine -> CashMachineInformation(historyItem)
    }
}

@Composable
fun CashMachineInformation(cashMachine: HistoryItem.CashMachine) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Icon(
            painter = painterResource(id = R.drawable.cash_machine),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.Top)
        )
        Column {
            Text(text = cashMachine.name, style = LabelAccentSemiBold, color = HistoryTextColor)
            Text(text = cashMachine.address, style = LabelAccentSemiBold, color = HistoryTextColor)
        }
        Text(text = cashMachine.date, style = AdditionalText, color = HistoryDateColor, modifier = Modifier.align(Alignment.Bottom))
    }
}

@Composable
fun BankBranchInformation(bankBranch: HistoryItem.BankBranch) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Icon(
            painter = painterResource(id = R.drawable.bank_branch),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.Top)
        )
        Column {
            Text(text = bankBranch.name, style = LabelAccentSemiBold, color = HistoryTextColor)
            Text(text = bankBranch.address, style = LabelAccentSemiBold, color = HistoryTextColor)
            Text(text = "Услуга: ${bankBranch.service}")
        }
        Text(
            text = bankBranch.date,
            style = AdditionalText,
            color = HistoryDateColor,
            modifier = Modifier.align(Alignment.Bottom)
        )
    }
}
