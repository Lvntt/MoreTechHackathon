package ru.phrogs.moretechhackathon.feature_visit_history.domain.model

sealed interface HistoryItem {
    data class CashMachine(
        val name: String,
        val address: String,
        val date: String,
    ) : HistoryItem

    data class BankBranch(
        val name: String,
        val address: String,
        val date: String,
        val service: String
    ) : HistoryItem
}