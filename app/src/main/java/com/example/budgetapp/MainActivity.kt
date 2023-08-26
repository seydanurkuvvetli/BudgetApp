package com.example.budgetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.budgetapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var bottomNavVisibility = View.VISIBLE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavVisibility = when (destination.id) {
                R.id.signInScreen, R.id.signUpScreen,R.id.splashScreen -> View.GONE
                else -> View.VISIBLE
            }
            setBottomNavVisibility(bottomNavVisibility)
        }
    }
    private fun setBottomNavVisibility(visibility: Int) {
        binding.bottomNavigationView.visibility = visibility
    }

}
