package com.nonoxy.foodies.app

import com.nonoxy.foodies.api.FoodiesApi
import com.nonoxy.foodies_main.eventbus.EventBusController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient? {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC)
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }

        return null
    }

    @Provides
    @Singleton
    fun provideFoodiesApi(okHttpClient: OkHttpClient?): FoodiesApi {
        return FoodiesApi(
            baseUrl = BuildConfig.FOOD_API_BASE_URL,
            okHttpClient = okHttpClient
        )
    }

    @Provides
    @Singleton
    fun provideCartEventBusController(): EventBusController {
        return EventBusController()
    }
}