package com.maruro.newspaper.recycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maruro.newspaper.R
import com.maruro.newspaper.enums.QueryEnums

class CountryAdapter(val countrySetting: String, val onClickListener: OnClickListener): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    private val countries = mutableListOf<QueryEnums.Country>()
    var lastRadioButton : RadioButton? = null

    class CountryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val flagImageView by lazy { itemView.findViewById<ImageView>(R.id.flagImageView) }
        val countryRadioButton by lazy{itemView.findViewById<RadioButton>(R.id.radioButton)}

        fun bind(country: QueryEnums.Country){
            Glide.with(itemView.context)
                .load("https://www.countryflags.io/${country.toString()}/flat/64.png")
                .placeholder(R.mipmap.ic_launcher)
                .fitCenter()
                .into(flagImageView)
            countryRadioButton.text = country.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
        if(countries[position].name == countrySetting){
            holder.countryRadioButton.setChecked(true)
            lastRadioButton = holder.countryRadioButton
        }
        holder.countryRadioButton.setOnClickListener {
            lastRadioButton?.setChecked(false)
            lastRadioButton = it as RadioButton
            onClickListener.onClick(it)
        }
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun setData(list: Array<QueryEnums.Country>){
        countries.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    interface OnClickListener{
        fun onClick(view: RadioButton)
    }
}