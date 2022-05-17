package es.uniovi.classicalscores.domain

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uniovi.classicalscores.data.ApiResult
import es.uniovi.classicalscores.data.Repository
import es.uniovi.classicalscores.ui.ScoresUIState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ScoresListViewModel: ViewModel() {

    init{
        getScoresList()
    }

    private val _scoresUIStateObservable = MutableLiveData<ScoresUIState>()
    val scoresUIStateObservable: LiveData<ScoresUIState> get() = _scoresUIStateObservable

    fun getScoresList(){
        viewModelScope.launch {
            Repository.updateScoresData()
                .map { result ->
                    when (result) {
                        is ApiResult.Success<*> -> ScoresUIState.Success(result.data!!)
                        is ApiResult.Error -> ScoresUIState.Error(result.message!!)
                        is ApiResult.Loading<*> -> ScoresUIState.Loading()
                    }
                }
                .collect {
                    _scoresUIStateObservable.value = it
                }
        }
    }

}