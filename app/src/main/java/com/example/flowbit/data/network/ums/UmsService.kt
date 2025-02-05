package com.example.flowbit.data.network.ums

import android.content.Context
import android.util.Log
import com.example.flowbit.R
import com.example.flowbit.data.network.bsp.ExchangeResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UmsService (val context: Context) {
    val TAG = "UmsService"

    // Retrofit 서비스 객체
    val umsService: IUmsService // IBoxOfficeService의 객체

    init {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(context.resources.getString(R.string.ums_url)) // url 입력
            .addConverterFactory(GsonConverterFactory.create()) // 가져온 json을 DTO로 parsing -> converter 이용해서 변환 -> gson 이용
            .build()

        // retrofit에 interface 구현 시킴 : IBoxOfficeService 객체 생성
        // IBoxOfficeService 내의 함수 사용 가능
        // Call<Root> 반환
        umsService = retrofit.create(IUmsService::class.java)
    }

    // suspend는 suspend안에서만 실행 가능함
    suspend fun verifyUserEmailAddress(request: VerifyUserEmailRequest)  : VerifyUserEmailResponse {
        // coroutine (Retrofit에서 Coroutine 자체 지원)
        // suspend 함수 호출
        val root : VerifyUserEmailResponse = umsService.verifyUserEmailAddress(request)
        return root

        // return null // response.body()?.boxOfficeResult?.boxOfficeList
    }

}