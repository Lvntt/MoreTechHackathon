package ru.phrogs.moretechhackathon.feature_visit_history.domain.repository

import ru.phrogs.moretechhackathon.feature_visit_history.domain.model.HistoryItem

interface HistoryRepository {
    fun saveHistoryItem(historyItem: HistoryItem)
    fun getHistory(): List<HistoryItem>
}