package com.example.flowbit.ui.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flowbit.databinding.ActivityRegister3Binding
import com.google.android.gms.common.internal.constants.ListAppsActivityContract

class Register3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRegister3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister3Binding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}