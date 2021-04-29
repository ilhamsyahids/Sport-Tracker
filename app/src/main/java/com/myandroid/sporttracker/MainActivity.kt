package com.myandroid.sporttracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateToIntent(intent)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)
        navView.setOnNavigationItemReselectedListener {}

        navController
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id) {
                    R.id.nav_home, R.id.nav_track, R.id.nav_schedule, R.id.nav_settings ->
                        navView.visibility = View.VISIBLE
                    else -> navView.visibility = View.GONE
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToIntent(intent)
    }

    private fun navigateToIntent(intent: Intent?) {
        if (intent?.action == TRACKING_ACTION_SHOW_TRACKING_FRAGMENT) {
            nav_host_fragment.findNavController().navigate(R.id.action_main_trackingFragment)
        }
    }
}