package mykotlin.info.appcryptocompare.api

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiFactory {

    companion object {
        const val BASE_IMAGE_URL = "https://cryptocompare.com"
    }

    private val baseURL = "https://min-api.cryptocompare.com/data/"

    @Provides
    fun provideApiServise(): ApiService {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseURL)
            .client(
                OkHttpClient()
                    .newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()

        return retrofit.create(ApiService::class.java)
    }
}