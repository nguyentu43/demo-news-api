package com.maruro.newspaper.di

import com.maruro.newspaper.ui.TopHeadlinesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TopHeadlinesFragmentProviderModule {
    @ContributesAndroidInjector(modules = [MainModule::class])
    @FragmentScope
    abstract fun contributeTopHeadlinesFragmentInjector(): TopHeadlinesFragment
}