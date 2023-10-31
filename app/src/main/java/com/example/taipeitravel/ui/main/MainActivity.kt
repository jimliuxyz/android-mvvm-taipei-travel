package com.example.taipeitravel.ui.main

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isEmpty
import androidx.core.view.size
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.taipeitravel.R
import com.example.taipeitravel.databinding.ActivityMainBinding
import com.example.taipeitravel.ui.main.viewmodels.HomeViewModel
import com.example.taipeitravel.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var menu: Menu? = null

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get navController of navHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        // control menu item by nav destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.startFragment) {
                Log.d("tag", "" + menu?.size)
                if (menu?.isEmpty() == true)
                    menuInflater.inflate(R.menu.main, menu)
            } else {
                menu?.clear()
            }
        }

        // bind navController action to actionbar
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    private fun isModeNightYes(): Boolean {
        val mode = AppCompatDelegate.getDefaultNightMode()
        if (mode < 0) {
            val nightModeFlags: Int = resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        }
        return !(mode == AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.main, menu)

        // init day/night color
        val item = menu!!.findItem(R.id.action_day_night) as MenuItem
        item.icon?.setTint(if (isModeNightYes()) Color.DKGRAY else Color.YELLOW)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            // ask to change locale
            R.id.action_lang -> {
                val listItems = arrayOf(
                    "zh-tw", "zh-cn", "en", "ja", "ko", "es", "id", "th", "vi"
                )
                val builder = AlertDialog.Builder(this@MainActivity)

                builder.setItems(
                    listItems.map { it -> Utils.getLangForDisplay(it) }.toTypedArray()
                ) { _, which ->
                    // set locale and refresh list
                    Utils.setAppLocales(listItems[which])
                    homeViewModel.fetch(reload = true)
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()
                return true
            }
            // ask to change day/night mode
            R.id.action_day_night -> {
                if (isModeNightYes()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    item.icon?.setTint(Color.YELLOW)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    item.icon?.setTint(Color.DKGRAY)
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}