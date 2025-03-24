package com.diego.futty.home.match.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.home.match.data.repository.LiveScoresRepositoryImpl
import com.diego.futty.home.match.domain.model.league.League
import com.diego.futty.home.match.domain.model.match.LiveMatch
import kotlinx.coroutines.launch

class MatchViewModel(
    private val liveScoresRepository: LiveScoresRepositoryImpl,
) : MatchViewContract, ViewModel() {

    private val _leagues = mutableStateOf<List<League>?>(null)
    override val leagues: State<List<League>?> = _leagues

    private val _liveScores = mutableStateOf<List<LiveMatch>?>(null)
    override val liveScores: State<List<LiveMatch>?> = _liveScores

    private val _todayScores = mutableStateOf<List<LiveMatch>?>(null)
    override val todayScores: State<List<LiveMatch>?> = _todayScores

    private val _buttonEnabled = mutableStateOf(true)
    override val buttonEnabled: State<Boolean> = _buttonEnabled

    private val _error = mutableStateOf("Cargando")
    override val error: State<String> = _error

    fun setup() {
        viewModelScope.launch {
            liveScoresRepository.getLeagues()
                .onSuccess { leagues ->
                    _leagues.value = leagues
                    if (leagues?.isEmpty() == true) {
                        _error.value = "No se pueden cargar las ligas en este momento."
                    }
                    getLiveScores()
                }
                .onError { error ->
                    _error.value = error.name
                }
        }
    }

    private fun getLiveScores() {
        viewModelScope.launch {
            liveScoresRepository.getLiveScores()
                .onSuccess { liveScores ->
                    _liveScores.value = liveScores
                    if (liveScores?.isEmpty() == true) {
                        _error.value = "No hay partidos en vivo en este momento."
                    }
                }
                .onError { error ->
                    _error.value = error.name
                }
        }
    }

    private fun getTodayScores() {
        viewModelScope.launch {
            liveScoresRepository.getTodayScores()
                .onSuccess { todayScores ->
                    _todayScores.value = todayScores
                    if (todayScores?.isEmpty() == true) {
                        _error.value = "No hay partidos hoy."
                    }
                }
                .onError { error ->
                    _error.value = error.name
                }
        }
    }

    override fun onButtonPressed() {
        _buttonEnabled.value = false
    }
}
