package com.berkaykbl.movist.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.berkaykbl.movist.R
import com.berkaykbl.movist.Utils
import com.berkaykbl.movist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
    }

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils().setDB(applicationContext)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_fragment)
        Utils().setNavController(navController)

        binding.tvseriesButton.setOnClickListener {
            if (navController.currentDestination!!.id != 1) {
                navController.navigate(R.id.action_to_TVSeries)
            }
        }
        binding.moviesButton.setOnClickListener {
            if (navController.currentDestination!!.id != 0) {
                navController.navigate(R.id.action_to_Movies)
            }
        }

        binding.bookmarkButton.setOnClickListener {
            if (navController.currentDestination!!.id != 2) {
                navController.navigate(R.id.action_to_Bookmark)
            }

        }

    }
}
