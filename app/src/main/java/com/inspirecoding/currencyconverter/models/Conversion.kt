package com.inspirecoding.currencyconverter.models

data class Conversion(
    var convertFrom: String = "",
    var convertTo: String = "",
    var rate: Double? = 0.0,
    var convertedValue: Double? = 0.0
)