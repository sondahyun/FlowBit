package com.example.flowbit.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flowbit.data.repository.RegisterRepository
import com.example.flowbit.ui.map.MapViewModel

class RegisterViewModelFactory (private val registerRepository: RegisterRepository): ViewModelProvider.Factory {
    // ViewModel 객체를 생성하는 함수를 재정의
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 생성하려는 클래스가 FoodViewModel 일 경우 객체 생성
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(registerRepository) as T
        }
        return IllegalArgumentException("Unknown ViewModel class") as T
    }
}