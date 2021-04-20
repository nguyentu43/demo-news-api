package com.maruro.newspaper.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.maruro.newspaper.R
import com.maruro.newspaper.enums.QueryEnums
import com.maruro.newspaper.viewModels.NewspaperViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val mainLayout by lazy<ConstraintLayout> { findViewById(R.id.main_layout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_search))
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController( navController, appBarConfiguration)
        findViewById<BottomNavigationView>(R.id.nav_view).setupWithNavController(navController)

        val newspaperViewModel = ViewModelProvider(this, factory).get(NewspaperViewModel::class.java)

        newspaperViewModel.error.observe(this, {
            val code = it.trim().toInt()
            Snackbar.make(mainLayout, "Error ${code}: ${QueryEnums.Error.valueOf("E${code}")}", Snackbar.LENGTH_INDEFINITE)
                .apply {
                    val snackbar: Snackbar = this
                    setAction("Close", object : View.OnClickListener {
                        override fun onClick(p0: View?) {
                            snackbar.dismiss()
                        }
                    })
                    show()
                }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting -> {
                Intent(this, CountrySelectActivity::class.java).let {
                    startActivity(it)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG: String = "MainActivity"
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}