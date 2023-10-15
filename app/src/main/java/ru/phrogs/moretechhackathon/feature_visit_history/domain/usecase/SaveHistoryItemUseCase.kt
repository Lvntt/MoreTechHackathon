package ru.phrogs.moretechhackathon.feature_visit_history.domain.usecase

import ru.phrogs.moretechhackathon.feature_visit_history.domain.model.HistoryItem
import ru.phrogs.moretechhackathon.feature_visit_history.domain.repository.HistoryRepository

class SaveHistoryItemUseCase(private val historyRepository: HistoryRepository) {
    operator fun invoke(historyItem: HistoryItem) {
        return historyRepository.saveHistoryItem(historyItem)
    }
}