package com.inspirecoding.currencyconverter.repository.remote

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrConvEndpoints {

    @GET("/api/v7/convert")
    fun getConvertedCurrency(
        @Query(value = "q") fromTo : String,
        @Query(value = "compact") compact : String,
        @Query(value = "apiKey") apiKey : String
    ): Flowable<Any>

}