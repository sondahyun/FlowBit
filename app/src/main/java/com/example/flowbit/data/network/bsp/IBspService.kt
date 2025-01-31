package com.example.flowbit.data.network.bsp

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IBspService {
    @GET("getAllCryptoList")
    suspend fun getExchanges(): ExchangeResponse

    @POST("sendBlockchainSignedTransaction")
    suspend fun sendSignedTransaction(@Body requestBody: BlockchainTransactionRequest): BlockchainTransactionResponse

}