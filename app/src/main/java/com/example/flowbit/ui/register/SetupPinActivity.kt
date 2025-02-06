package com.example.flowbit.ui.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flowbit.databinding.ActivitySetupPinBinding

class SetupPinActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetupPinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupPinBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}