package com.whywhom.soft.whyradiobox.data

import android.os.Build
import com.google.gson.GsonBuilder
import com.tanzhiqiang.kmvvm.ext.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import java.io.IOException
import java.util.*

/**
 * Created by wuhaoyong on 2020-01-12.
 */
object HttpRepository {

    const val itunesBaseUrl = "https://itunes.apple.com/"

//    private val httpClient = OkHttpClient.Builder()
//        .addInterceptor(LoggingInterceptor())
//        .addInterceptor(HeaderInterceptor())
//        .addInterceptor(BasicParamsInterceptor())
//        .build()
//
//    fun provideOKhttpService(
//    ): OkHttpClient {
//        return httpClient
//    }
//
    class LoggingInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val t1 = System.nanoTime()
            Log.v(TAG, "Sending request: ${request.url()} \n ${request.headers()}")

            val response = chain.proceed(request)

            val t2 = System.nanoTime()
            Log.v(TAG, "Received response for  ${response.request().url()} in ${(t2 - t1) / 1e6} ms\n${response.headers()}")
            return response
        }

        companion object {
            const val TAG = "LoggingInterceptor"
        }
    }

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder().apply {
                header("model", "Android")
                header("If-Modified-Since", "${Date()}")
                header("User-Agent", System.getProperty("http.agent") ?: "unknown")
            }.build()
            return chain.proceed(request)
        }
    }

    class BasicParamsInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url()
            val url = originalHttpUrl.newBuilder().apply {
                addQueryParameter("system_version_code", "${Build.VERSION.SDK_INT}")
            }.build()
            val request = originalRequest.newBuilder().url(url).method(originalRequest.method(), originalRequest.body()).build()
            return chain.proceed(request)
        }
    }
private fun getApiService(): NetworkApiService {
    return Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(provideOkHttpClient(provideLoggingInterceptor()))
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(NetworkApiService::class.java)
}

    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder().apply {
//        addInterceptor(interceptor)
        addInterceptor(LoggingInterceptor())
        addInterceptor(HeaderInterceptor())
        addInterceptor(BasicParamsInterceptor())
    }.build()

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }

    fun getTopList(lang:String, limit:String) = getApiService().getTopList(lang, limit)
    fun search(artist: String) = getApiService().search(artist)
}