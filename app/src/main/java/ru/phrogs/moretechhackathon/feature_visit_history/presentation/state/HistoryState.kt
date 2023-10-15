package ru.phrogs.moretechhackathon.feature_visit_history.presentation.state
import ru.phrogs.moretechhackathon.feature_visit_history.domain.model.HistoryItem

data class HistoryState(
    val historyItems: List<HistoryItem>
)
