package com.inspirecoding.currencyconverter.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import com.bumptech.glide.Glide
import com.inspirecoding.currencyconverter.databinding.ItemSpinnerBinding


class SpinnerAdapter (
    private val layoutInflater: LayoutInflater,
    private val countries: List<Pair<Int, String>>
): BaseAdapter() {

    override fun getCount() = countries.size

    override fun getItem(position: Int) = countries[position]

    override fun getItemId(position: Int) = countries[position].first.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val binding = ItemSpinnerBinding.inflate(layoutInflater)

        if(countries[position].first != 0) {
            Glide.with(binding.root)
                .load(ContextCompat.getDrawable(binding.root.context, countries[position].first))
                .into(binding.ivCountryFlag)
        } else {
            binding.root.isInvisible = true
        }

        binding.tvCountryName.text = countries[position].second

        return binding.root
    }


}