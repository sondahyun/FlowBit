package com.example.flowbit.data.network

import retrofit2.http.GET

interface IExchangeService {
    @GET("getAllCryptoList")
    suspend fun getExchanges(): ExchangeResponse
}