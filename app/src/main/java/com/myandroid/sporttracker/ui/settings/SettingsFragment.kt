package com.myandroid.sporttracker.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.SettingsViewModel
import com.myandroid.sporttracker.util.Constant.KEY_HEIGHT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject
    lateinit var sharedPref: SharedPreferences

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        loadSharedPref()

        btnSave.setOnClickListener {
            if (saveSharedPref()) {
                Toast.makeText(
                    this.context,
                    "Saved successfully",
                    Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(
                    this.context,
                    "Please fill the height form correctly",
                    Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun loadSharedPref() {
        val h = sharedPref.getInt(KEY_HEIGHT, 0).toString()
        if (!isEmptyOrZero(h)) {
            tiHeight.setText(h)
        }
    }

    private fun isEmptyOrZero(x: String): Boolean {
        return x.isEmpty() || x == "0"
    }

    private fun saveSharedPref(): Boolean {
        val height = tiHeight.text.toString()

        if (isEmptyOrZero(height)) {
            return false
        }

        sharedPref.edit()
            .putInt(KEY_HEIGHT, height.toInt())
            .apply()

        return true
    }

}