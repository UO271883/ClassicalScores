package es.uniovi.classicalscores.ui

import es.uniovi.classicalscores.model.Pieza
import es.uniovi.classicalscores.state.AppStatus

sealed class ScoresUIState (val state: AppStatus) {
    data class Success (val piezas: Array<Pieza>): ScoresUIState(AppStatus.SUCCESS)
    data class Error (val message: String): ScoresUIState(AppStatus.ERROR)
}
