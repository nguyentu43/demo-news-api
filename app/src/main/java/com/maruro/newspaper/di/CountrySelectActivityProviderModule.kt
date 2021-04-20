package com.maruro.newspaper.di

import com.maruro.newspaper.ui.CountrySelectActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CountrySelectActivityProviderModule {
    @ContributesAndroidInjector(modules = [MainModule::class])
    @ActivityScope
    abstract fun contributeCountrySelectActivityInjector(): CountrySelectActivity
}