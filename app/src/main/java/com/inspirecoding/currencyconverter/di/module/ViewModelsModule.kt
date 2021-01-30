package com.inspirecoding.currencyconverter.di.module

import androidx.lifecycle.ViewModel
import com.inspirecoding.currencyconverter.di.ViewModelKey
import com.inspirecoding.currencyconverter.viewmodel.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel (viewModel: MainActivityViewModel) : ViewModel

}