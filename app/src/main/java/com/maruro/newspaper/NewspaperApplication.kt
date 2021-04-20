package com.maruro.newspaper

import android.app.Application
import com.maruro.newspaper.di.ApplicationComponent
import com.maruro.newspaper.di.ApplicationModule
import com.maruro.newspaper.di.DaggerApplicationComponent
import com.maruro.newspaper.repositories.NewspaperRepository
import com.maruro.newspaper.utils.PreferencesWrapper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class NewspaperApplication: Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var newspaperRepository: NewspaperRepository
    @Inject
    lateinit var preferencesWrapper: PreferencesWrapper

    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(
            ApplicationModule(applicationContext)).build()
        applicationComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}