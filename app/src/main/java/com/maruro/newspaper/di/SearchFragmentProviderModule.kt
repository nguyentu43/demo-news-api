package com.maruro.newspaper.di

import com.maruro.newspaper.ui.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SearchFragmentProviderModule {
    @ContributesAndroidInjector(modules = [MainModule::class])
    @FragmentScope
    abstract fun contributeSearchFragmentInjector(): SearchFragment
}