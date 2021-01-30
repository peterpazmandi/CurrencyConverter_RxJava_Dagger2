package com.inspirecoding.currencyconverter.repository

import io.reactivex.Flowable

interface CurrencyRepository {

    fun getConvertedCurrency(
        fromTo : String,
        compact : String,
        apiKey : String
    ): Flowable<Any>

}