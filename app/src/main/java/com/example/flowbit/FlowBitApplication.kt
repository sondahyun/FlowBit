package com.example.flowbit

import android.app.Application
import com.example.flowbit.data.database.AppDatabase
import com.example.flowbit.data.map.MapService
import com.example.flowbit.data.network.bsp.BspService
import com.example.flowbit.data.network.ums.UmsService
import com.example.flowbit.data.repository.ExchangeRepository
import com.example.flowbit.data.repository.ExpenseRepository
import com.example.flowbit.data.repository.IncomeRepository
import com.example.flowbit.data.repository.MapRepository
import com.example.flowbit.data.repository.RegisterRepository

class FlowBitApplication : Application() {
    // Lazy initialization for database
    val database by lazy {
        AppDatabase.getDatabase(this)
    }

    // Lazy initialization for repositories
    val expenseRepository by lazy {
        ExpenseRepository(database.expenseDao())
    }

    val incomeRepository by lazy {
        IncomeRepository(database.incomeDao())
    }

    val bspService by lazy {
        BspService(this)
    }

    val umsService by lazy {
        UmsService(this)
    }

    val registerRepository by lazy {
        RegisterRepository(umsService)
    }

    val exchangeRepository by lazy {
        ExchangeRepository(bspService)
    }

    val mapService by lazy {
        MapService(this)
    }

    val mapRepository by lazy {
        MapRepository(mapService)
    }
}