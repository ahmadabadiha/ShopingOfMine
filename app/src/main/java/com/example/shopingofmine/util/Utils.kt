package com.example.shopingofmine.util

import android.util.Log
import com.example.shopingofmine.model.serverdataclass.ServerError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.Flow
import javax.net.ssl.SSLException

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
                    400 -> errorMessage = " درخواست شما نامعتبر است "
                    401 -> errorMessage = " درخواست غیر مجاز "
                    404 -> errorMessage = " پاسخ مطابق با درخواست شما یافت نشد "
                    500 -> errorMessage = " سرور با مشکل مواجه شده است "
                }
                emit(ResultWrapper.Error(errorMessage + responseError.message))
            } else {
                emit(ResultWrapper.Error<T>("خطای غیرمنتظره"))
            }
            //todo
        }
    } catch (e: SSLException) {
        emit(ResultWrapper.Error(e.message))
    } catch (e: IOException) {
        emit(ResultWrapper.Error(e.message))
    } catch (e: HttpException) {
        emit(ResultWrapper.Error(e.message))
    } catch (e: Throwable) {
        emit(ResultWrapper.Error(e.message))
    } finally {
        // emit(ResultWrapper.Error("finally you .."))
    }
}
