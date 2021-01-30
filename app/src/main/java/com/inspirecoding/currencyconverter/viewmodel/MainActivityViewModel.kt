package com.inspirecoding.currencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspirecoding.currencyconverter.BuildConfig
import com.inspirecoding.currencyconverter.models.Conversion
import com.inspirecoding.currencyconverter.repository.CurrencyRepository
import com.inspirecoding.currencyconverter.utils.COMPACT_TYPE_ULTRA
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
): ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val _events = Channel<Events>()
    val events = _events.receiveAsFlow()

    private lateinit var conversionResultObservable: Flowable<Any>
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    var amountToConvert = 0
    var currencyFrom = ""
    var currencyTo = ""


    fun getConversion() {
        var errormessage = ""

        if(amountToConvert == 0) {
            errormessage += "Enter an amount\n"
        }
        if(currencyFrom.isEmpty()) {
            errormessage += "Select a currency type\n"
        }
        if(currencyTo.isEmpty()) {
            errormessage += "Select a converted currency type"
        }

        if(errormessage.isEmpty()) {
            subscribeObservableConversionResult()
        } else {
            viewModelScope.launch {
                _events.send(Events.ShowErrorMessage(errormessage))
            }
        }
    }

    private fun subscribeObservableConversionResult() {
        viewModelScope.launch {
            _events.send(Events.ShowLoading)
        }

        conversionResultObservable = currencyRepository.getConvertedCurrency(
            "${currencyFrom}_${currencyTo}", COMPACT_TYPE_ULTRA, BuildConfig.API_KEY
        )

        val result = conversionResultObservable
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { conversionResult ->
                    val conversion = createConversion(conversionResult.toString())
                    calculateFinalResult(conversion)
                },
                onError = { throwable ->
                    throwable.message?.let { _message ->
                        viewModelScope.launch {
                            _events.send(Events.ShowErrorMessage(_message))
                        }
                    }
                },
                onComplete = {

                }
            )

        compositeDisposable.add(result)
    }

    private fun createConversion(result: String): Conversion {
        return Conversion(
            convertFrom = currencyFrom,
            convertTo = currencyTo,
            rate = result.substringAfter("=", "").substringBefore("}", "").toDoubleOrNull()
        )
    }

    private fun calculateFinalResult(conversion: Conversion) {
        viewModelScope.launch {
            conversion.convertedValue = conversion.rate?.times(amountToConvert)
            _events.send(Events.ShowResult(conversion))
        }
    }


    sealed class Events {
        object ShowLoading : Events()
        data class ShowResult(val resultUnit: Conversion): Events()
        data class ShowErrorMessage(val message: String): Events()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}