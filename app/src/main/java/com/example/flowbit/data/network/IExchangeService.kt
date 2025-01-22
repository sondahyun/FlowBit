package com.example.flowbit.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IExchangeService {
    @GET("getAllCryptoList")
    suspend fun getExchanges(): ExchangeResponse

    @POST("sendBlockchainSignedTransaction")
    suspend fun sendSignedTransaction(@Body requestBody: BlockchainTransactionRequest): BlockchainTransactionResponse

}