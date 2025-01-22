package com.example.flowbit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flowbit.databinding.ActivityMainBinding
import com.example.flowbit.ui.home.HomeFragment
import com.example.flowbit.ui.calendar.CalendarFragment
import com.example.flowbit.ui.exchange.MoneyFragment
import com.example.flowbit.ui.map.MapFragment
import com.example.flowbit.ui.mypage.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(binding.root)

        val navigateTo = intent.getStringExtra("navigate_to")
        binding.bottomNavigationView.setItemIconTintList(null)

        if (navigateTo == "HomeFragment") {
            showHomeFragment()
        } else if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_home
            showHomeFragment()
        }

        setBottomNavigationView()
    }

    private fun showHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, HomeFragment())
            .commit()
    }

    private fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_home -> {
                    showHomeFragment()
                    true
                }

                R.id.fragment_calendar -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, CalendarFragment())
                        .commit()
                    true
                }

                R.id.fragment_money -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, MoneyFragment())
                        .commit()
                    true
                }

                R.id.fragment_map -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, MapFragment())
                        .commit()
                    true
                }

                R.id.fragment_mypage -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, MyPageFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}