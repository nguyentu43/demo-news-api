package com.maruro.newspaper.di

import android.content.Context
import android.content.SharedPreferences
import com.maruro.newspaper.api.ApiService
import com.maruro.newspaper.repositories.NewspaperRepository
import com.maruro.newspaper.utils.PreferencesWrapper
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class ApplicationModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return  retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewspaperRepository(apiService: ApiService): NewspaperRepository = NewspaperRepository(
        apiService
    )

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences = context.getSharedPreferences(
        "prefs",
        Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun providePreferencesWrapper(preferences: SharedPreferences): PreferencesWrapper = PreferencesWrapper(
        preferences
    )
}