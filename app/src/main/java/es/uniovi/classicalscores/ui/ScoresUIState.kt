package es.uniovi.classicalscores.ui

import es.uniovi.classicalscores.model.Pieza
import es.uniovi.classicalscores.state.AppStatus

sealed class ScoresUIState (val state: AppStatus) {
    data class Success (val piezas: Array<Pieza>): ScoresUIState(AppStatus.SUCCESS) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Success

            if (!piezas.contentEquals(other.piezas)) return false

            return true
        }

        override fun hashCode(): Int {
            return piezas.contentHashCode()
        }
    }

    data class Error (val message: String): ScoresUIState(AppStatus.ERROR)
    data class Loading (val loading: Boolean = true): ScoresUIState(AppStatus.LOADING)
}
