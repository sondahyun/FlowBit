package com.example.flowbit.data.repository

import android.util.Log
import com.example.flowbit.data.network.bsp.Exchange
import com.example.flowbit.data.network.bsp.ExchangeResponse
import com.example.flowbit.data.network.bsp.BspService

class ExchangeRepository(private val bspService: BspService) {

    suspend fun getExchanges(): List<Exchange>? {
        return try {
            Log.d("ExchangeRepository", "서버 데이터 요청 중")
            val response: ExchangeResponse = bspService.getExchanges()
            Log.d("ExchangeRepository", "서버 응답 성공: ${response.data.value}")
            response.data.value // 데이터 리스트 반환
        } catch (e: Exception) {
            Log.e("ExchangeRepository", "데이터 요청 실패: ${e.message}")
            null
        }
    }
}