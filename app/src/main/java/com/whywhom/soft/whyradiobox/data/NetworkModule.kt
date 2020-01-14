package com.whywhom.soft.whyradiobox.data

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by wuhaoyong on 2020-01-12.
 */
@Module
class NetworkModule {
    val itunesBaseUrl = "https://itunes.apple.com/"
    private var ipService: NetworkApiService
    private var logging: HttpLoggingInterceptor

    companion object {
        //此类接口的基地址
        private lateinit var retrofit: Retrofit
        private lateinit var client: OkHttpClient
    }

    init{
        logging = HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = OkHttpClient.Builder().addInterceptor(logging).build();
        retrofit = Retrofit.Builder()
            .baseUrl(itunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        ipService = retrofit.create(NetworkApiService::class.java)
    }

    @Provides
    fun provideRetrofitService(
    ): NetworkApiService {
        return ipService
    }
}