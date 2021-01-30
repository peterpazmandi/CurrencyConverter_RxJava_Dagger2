package com.inspirecoding.currencyconverter.di.module

import androidx.lifecycle.ViewModelProvider
import com.inspirecoding.currencyconverter.viewmodelprovider.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun providesViewModelFactory(viewModelFactory: ViewModelFactory) : ViewModelProvider.Factory

}