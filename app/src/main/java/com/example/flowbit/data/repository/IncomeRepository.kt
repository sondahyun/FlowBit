package com.example.flowbit.data.repository

import com.example.flowbit.data.dao.IncomeDao
import com.example.flowbit.data.entity.Income
import kotlinx.coroutines.flow.Flow

class IncomeRepository(private val incomeDao: IncomeDao) {

    suspend fun insertIncome(income: Income) {
        incomeDao.insertIncome(income)
    }

    // 해당 하는 날의 모든 수익
    fun getIncomesByDate(date: String): Flow<List<Income>> {
        return incomeDao.getIncomesByDate(date)
    }

    // 해당 하는 날의 총 수익
    fun getTotalIncomeByDate(date: String): Flow<Int> {
        return incomeDao.getTotalIncomeByDate(date)
    }

    // 해당 하는 _id 의 수익 삭제
    suspend fun deleteIncomeById(id: Int) {
        incomeDao.deleteIncomeById(id)
    }

    // 해당 하는 date 의 모든 수익 삭제
    suspend fun deleteIncomesByDate(date: String) {
        incomeDao.deleteIncomesByDate(date)
    }

    // Delete an entire income object
    suspend fun deleteIncome(income: Income) {
        incomeDao.deleteIncome(income)
    }
}