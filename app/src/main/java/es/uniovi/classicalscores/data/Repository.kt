package es.uniovi.classicalscores.data

import android.graphics.Bitmap
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import es.uniovi.classicalscores.network.RestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object Repository {

    fun updateScoresData() =
        // Se crea un flujo
        flow {
            // Se realiza la petición al servicio
            try {
                // Respuesta correcta
                emit(ApiResult.Loading(null))
                val scores = RestApi.retrofitService.getScoresInfo()
                // Se emite el estado Succes y se incluyen los datos recibidos
                emit(ApiResult.Success(scores))
            } catch (e: Exception) {
                // Error en la red
                // Se emite el estado de Error con el mensaje que lo explica
                emit(ApiResult.Error(e.toString()))
            }
            // El flujo se ejecuta en el hilo I/O
        }.flowOn(Dispatchers.IO)

}