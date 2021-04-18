package com.myandroid.sporttracker

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)

        navController
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id) {
                    R.id.nav_home, R.id.nav_track, R.id.nav_scheduler ->
                        navView.visibility = View.VISIBLE
                    else -> navView.visibility = View.GONE
                }
            }
    }
}