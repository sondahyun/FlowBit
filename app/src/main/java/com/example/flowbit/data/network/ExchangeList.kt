package com.example.flowbit.data.network

import com.google.gson.annotations.SerializedName

data class ExchangeResponse(
    @SerializedName("success")
    val success: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val data: ExchangeData,
    val msg: String
)

data class ExchangeData(
    @SerializedName("property")
    val property: String,
    @SerializedName("value")
    val value: List<Exchange>,
    val options: Map<String, Any>
)

typealias ExchangeList = List<Exchange>

data class Exchange(
    @SerializedName("symbol_list_id")
    val symbolListId: Long,
    @SerializedName("blockchain_name")
    val blockchainName: String,
    @SerializedName("crypto_asset_type")
    val cryptoAssetType: Long,
    @SerializedName("crypto_symbol")
    val cryptoSymbol: String,
    @SerializedName("crypto_icon")
    val cryptoIcon: String,
    @SerializedName("crypto_name")
    val cryptoName: String,
    @SerializedName("contract_address")
    val contractAddress: String?,
    @SerializedName("crypto_decimal")
    val cryptoDecimal: Long,
    @SerializedName("won")
    val won: Double,
    @SerializedName("rate")
    val rate: Double,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("supported")
    val supported: Int
)