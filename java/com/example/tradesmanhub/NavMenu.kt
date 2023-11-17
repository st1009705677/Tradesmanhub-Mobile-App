package com.example.tradesmanhub

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.tradesmanhub.fragments.HomeFragment
import com.example.tradesmanhub.fragments.ProfileFragment
import com.example.tradesmanhub.fragments.SearchFragment
import com.example.tradesmanhub.fragments.SettingsFragment

@Suppress("DEPRECATION", "LocalVariableName", "DUPLICATE_LABEL_IN_WHEN")
class NavMenu : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val profileFragment = ProfileFragment()
    private val settingsFragment = SettingsFragment()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_menu)

        // Initialize bottom navigation
        bottomNav = findViewById(R.id.bottomNav)

        setCurrentFragment(homeFragment)

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.search -> setCurrentFragment(searchFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
                R.id.settings -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
}
