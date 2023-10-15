package ru.phrogs.moretechhackathon.feature_visit_history.data.repository

import ru.phrogs.moretechhackathon.feature_visit_history.data.local.HistoryStorage
import ru.phrogs.moretechhackathon.feature_visit_history.domain.model.HistoryItem
import ru.phrogs.moretechhackathon.feature_visit_history.domain.repository.HistoryRepository

class HistoryRepositoryImpl(private val historyStorage: HistoryStorage): HistoryRepository {
    override fun saveHistoryItem(historyItem: HistoryItem) {
        historyStorage.saveHistoryItem(historyItem)
    }

    override fun getHistory(): List<HistoryItem> {
        return historyStorage.getHistory()
    }
}