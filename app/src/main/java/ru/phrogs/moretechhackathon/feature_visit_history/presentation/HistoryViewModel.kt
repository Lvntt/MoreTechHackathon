package ru.phrogs.moretechhackathon.feature_visit_history.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.phrogs.moretechhackathon.feature_visit_history.domain.usecase.GetHistoryUseCase
import ru.phrogs.moretechhackathon.feature_visit_history.presentation.state.HistoryState

class HistoryViewModel(private val getHistoryUseCase: GetHistoryUseCase) : ViewModel() {

    private val _state = mutableStateOf(HistoryState(getHistoryUseCase()))
    val state: State<HistoryState> = _state

    fun loadHistory() {
        _state.value = HistoryState(historyItems = getHistoryUseCase())
    }

    fun backFromHistory() {
        Unit
    }
}