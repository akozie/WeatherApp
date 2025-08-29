package com.infinion.infinion.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.infinion.infinion.R
import com.infinion.infinion.databinding.ActivityHomeBinding
import com.infinion.infinion.ui.fragment.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Setup Navigation
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupViews()

        onBackPressedDispatcher.addCallback(this) {
            val currentFragment = navHostFragment.childFragmentManager.fragments.firstOrNull()

            if (currentFragment is HomeFragment) {
                // Exit app if you're in the main fragment
                finishAffinity()
            } else {
                // Navigate up in the back stack
                findNavController(R.id.nav_host_fragment).navigateUp()
            }
        }

    }


    private fun setupViews() {
        binding.bottomNavigation.setupWithNavController(
            navHostFragment.findNavController(),
        )

        // Manually handle Home button clicks to reset back stack
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // If already on HomeFragment, pop back stack to prevent ProfileFragment from appearing
                    if (navController.currentDestination?.id != R.id.homeFragment) {
                        navController.popBackStack(R.id.homeFragment, false)
                    }
                    true
                }

                else -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                    true
                }
            }
        }

        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    // Check if already on HomeFragment
                    if (navController.currentDestination?.id != R.id.homeFragment) {
                        navController.popBackStack(R.id.homeFragment, false) // Clear back stack
                    }
                    binding.bottomNavigation.visibility = View.VISIBLE
                }

                else -> binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }
}