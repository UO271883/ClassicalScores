package es.uniovi.classicalscores.data

import es.uniovi.classicalscores.state.AppStatus

sealed class ApiResult <out T> (val status: AppStatus, val data: T?, val message:String?) {

    data class Success<out R>(val _data: R): ApiResult<R>(
        status = AppStatus.SUCCESS,
        data = _data,
        message = null
    )

    data class Error(val exception: String): ApiResult<Nothing>(
        status = AppStatus.ERROR,
        data = null,
        message = exception
    )

    data class Loading<out R>(val _data: R?): ApiResult<R>(
        status = AppStatus.LOADING,
        data = _data,
        message = "Loading..."
    )
}
