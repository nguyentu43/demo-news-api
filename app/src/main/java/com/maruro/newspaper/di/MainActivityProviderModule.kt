package com.maruro.newspaper.di

import com.maruro.newspaper.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityProviderModule {
    @ContributesAndroidInjector(modules = [MainModule::class])
    @ActivityScope
    abstract fun contributeMainActivityInjector(): MainActivity
}