package me.yeahapps.mypetai.core.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.UnknownHostException

class NetworkErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: UnknownHostException) {
            throw NoInternetException("Нет подключения к интернету", e)
        } catch (e: IOException) {
            throw NetworkException("Ошибка сети", e)
        }
    }
}

class NoInternetException(message: String, cause: Throwable) : IOException(message, cause)
class NetworkException(message: String, cause: Throwable) : IOException(message, cause)
