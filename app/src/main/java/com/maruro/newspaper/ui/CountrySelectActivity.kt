package com.maruro.newspaper.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maruro.newspaper.R
import com.maruro.newspaper.enums.QueryEnums
import com.maruro.newspaper.recycleView.CountryAdapter
import com.maruro.newspaper.utils.PreferencesWrapper
import dagger.android.AndroidInjection
import javax.inject.Inject

class CountrySelectActivity : AppCompatActivity() {

    private val countrySelectButton by lazy { findViewById<Button>(R.id.countrySelectbutton) }

    @Inject
    lateinit var preferencesWrapper: PreferencesWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_select)

        AndroidInjection.inject(this)

        val countrySetting = preferencesWrapper.getValue(PreferencesWrapper.COUNTRY_SETTING, QueryEnums.Country.USA.name)
        val adapter = CountryAdapter(countrySetting, object: CountryAdapter.OnClickListener{
            override fun onClick(view: RadioButton) {
                preferencesWrapper.setValue(PreferencesWrapper.COUNTRY_SETTING, view.text.toString())
            }
        })
        val recyclerView = findViewById<RecyclerView>(R.id.countryRecycleView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.setData(QueryEnums.Country.values())

        countrySelectButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}