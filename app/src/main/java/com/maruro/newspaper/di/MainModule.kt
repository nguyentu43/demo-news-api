package com.maruro.newspaper.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maruro.newspaper.repositories.NewspaperRepository
import com.maruro.newspaper.viewModels.NewspaperViewModel
import dagger.Module
import dagger.Provides

@Module
class MainModule {
    @Provides
    fun provideMainViewModelFactory(newspaperRepository: NewspaperRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NewspaperViewModel(newspaperRepository) as T
        }
    }
}