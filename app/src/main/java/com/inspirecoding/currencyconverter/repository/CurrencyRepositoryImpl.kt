package com.inspirecoding.currencyconverter.repository

import com.inspirecoding.currencyconverter.repository.remote.CurrConvEndpoints
import io.reactivex.Flowable
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currConvEndpoints: CurrConvEndpoints
): CurrencyRepository {

    override fun getConvertedCurrency(
        fromTo: String,
        compact: String,
        apiKey: String
    ): Flowable<Any> = currConvEndpoints.getConvertedCurrency(
        fromTo, compact, apiKey
    )
}