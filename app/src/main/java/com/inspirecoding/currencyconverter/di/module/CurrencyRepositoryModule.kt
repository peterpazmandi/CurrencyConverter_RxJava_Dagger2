package com.inspirecoding.currencyconverter.di.module

import com.inspirecoding.currencyconverter.repository.CurrencyRepository
import com.inspirecoding.currencyconverter.repository.CurrencyRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class CurrencyRepositoryModule {

    @Binds
    abstract fun providesCurrencyRepository (
        currencyRepositoryImpl: CurrencyRepositoryImpl
    ): CurrencyRepository

}