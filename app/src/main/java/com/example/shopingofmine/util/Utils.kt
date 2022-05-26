package com.example.shopingofmine.util

import android.util.Log
import com.example.shopingofmine.model.serverdataclass.ServerError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error<out T>(val message: String?) : ResultWrapper<T>()
    object Loading : ResultWrapper<Nothing>()
}

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> Response<T>
) = flow {
    emit(ResultWrapper.Loading)
    try {
        val response = apiCall()
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            emit(ResultWrapper.Success(responseBody))
        } else {
            val errorBody = response.errorBody()
            if (errorBody != null) {
                val type = object : TypeToken<ServerError>() {}.type
                val responseError = Gson().fromJson<ServerError>(errorBody.charStream(), type)
                var errorMessage = ""
                when (response.code()) {
                    400 -> errorMessage = " درخواست ارسال شده نامعتبر است "
                    401 -> errorMessage = " درخواست غیر مجاز "
                    404 -> errorMessage = " پاسخ مطابق با درخواست شما یافت نشد "
                    500 -> errorMessage = " سرور با مشکل مواجه شده است. چند دقیقه دیگر دوباره امتحان کنید. "
                }
                emit(ResultWrapper.Error(errorMessage + responseError.message))
            } else {
                emit(ResultWrapper.Error("خطای غیرمنتظره"))
            }

        }
    } catch (e: IOException) {
        emit(ResultWrapper.Error("امکان برقراری ارتباط با سرور وجود ندارد :${e.message}"))
    }  catch (e: Throwable) {
        Log.d("ahmad", "safeApiCall: th")
        emit(ResultWrapper.Error("خطای غیرمنتظره :${e.message}"))
    }
}
