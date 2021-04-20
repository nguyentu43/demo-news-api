package com.maruro.newspaper.di

import com.maruro.newspaper.NewspaperApplication
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ApplicationModule::class,
        MainActivityProviderModule::class, CountrySelectActivityProviderModule::class,
        TopHeadlinesFragmentProviderModule::class, SearchFragmentProviderModule::class])

interface ApplicationComponent {
    fun inject(application: NewspaperApplication)
}
