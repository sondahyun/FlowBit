package com.example.flowbit

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flowbit.databinding.ActivityMainBinding
import com.example.flowbit.ui.calendar.CalendarFragment
import com.example.flowbit.ui.exchange.MoneyFragment
import com.example.flowbit.ui.home.HomeFragment
import com.example.flowbit.ui.login.LoginActivity
import com.example.flowbit.ui.map.MapFragment
import com.example.flowbit.ui.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // 로그인 상태 확인
        if (!isUserLoggedIn()) {
            navigateToLogin()
            return
        }

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

    private fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
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
