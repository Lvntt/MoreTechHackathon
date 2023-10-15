package ru.phrogs.moretechhackathon.feature_visit_history.domain.usecase

import ru.phrogs.moretechhackathon.feature_visit_history.domain.model.HistoryItem
import ru.phrogs.moretechhackathon.feature_visit_history.domain.repository.HistoryRepository

class GetHistoryUseCase(private val historyRepository: HistoryRepository) {
    operator fun invoke(): List<HistoryItem> {
        return historyRepository.getHistory()
    }
}