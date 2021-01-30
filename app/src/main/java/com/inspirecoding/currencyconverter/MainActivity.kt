package com.inspirecoding.currencyconverter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inspirecoding.currencyconverter.adapter.SpinnerAdapter
import com.inspirecoding.currencyconverter.databinding.ActivityMainBinding
import com.inspirecoding.currencyconverter.utils.listOfCountriesWithFlags
import com.inspirecoding.currencyconverter.viewmodel.MainActivityViewModel
import com.inspirecoding.currencyconverter.viewmodelprovider.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private val TAG = this.javaClass.simpleName

    private lateinit var binding : ActivityMainBinding
    private val spinnerAdapter: SpinnerAdapter by lazy {
        val sortedList = listOfCountriesWithFlags.sortedBy {
            it.second
        }
        SpinnerAdapter(layoutInflater, sortedList)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        initSpinners()

        setupEvent()

        binding.tietAmountToConvert.doAfterTextChanged { amount ->
            if(!amount?.toString().isNullOrEmpty()) {
                viewModel.amountToConvert = amount.toString().toInt()
            }
        }

        binding.btnConvert.setOnClickListener {
            viewModel.getConversion()
        }
    }

    private fun setupEvent() {
        this.lifecycleScope.launchWhenStarted {
            viewModel.events.collect { event ->
                when(event)
                {
                    MainActivityViewModel.Events.ShowLoading -> {
                        binding.btnConvert.isVisible = false
                        binding.progressBar.isVisible = true
                    }
                    is MainActivityViewModel.Events.ShowResult -> {
                        binding.btnConvert.isVisible = true
                        binding.progressBar.isVisible = false

                        binding.tvUnitPrice.text = this@MainActivity.getString(
                            R.string.unit_price, "1", event.resultUnit.convertFrom, event.resultUnit.rate.toString(), event.resultUnit.convertTo
                        )
                        binding.tietConvertedAmount.setText(event.resultUnit.convertedValue.toString())
                    }
                    is MainActivityViewModel.Events.ShowErrorMessage -> {
                        binding.btnConvert.isVisible = true
                        binding.progressBar.isVisible = false

                        Toast.makeText(
                            this@MainActivity, event.message, Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun initSpinners() {
        binding.spinnerConvertFrom.adapter = spinnerAdapter
        binding.spinnerConvertTo.adapter = spinnerAdapter

        binding.spinnerConvertFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                val selectedItem = adapterView?.getItemAtPosition(position) as Pair<Int, String>
                viewModel.currencyFrom = selectedItem.second
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        binding.spinnerConvertTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                val selectedItem = adapterView?.getItemAtPosition(position) as Pair<Int, String>
                viewModel.currencyTo = selectedItem.second
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}