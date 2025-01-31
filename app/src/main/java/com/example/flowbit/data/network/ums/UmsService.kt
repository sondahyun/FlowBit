package com.example.flowbit.data.network.ums

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UmsService (val context: Context) {
    val TAG = "UmsService"

    // Retrofit 서비스 객체
    val umsService: IUmsService // IBoxOfficeService의 객체

    init {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://bsp.ltcwareko.com/") // url 입력
            .addConverterFactory(GsonConverterFactory.create()) // 가져온 json을 DTO로 parsing -> converter 이용해서 변환 -> gson 이용
            .build()

        // retrofit에 interface 구현 시킴 : IBoxOfficeService 객체 생성
        // IBoxOfficeService 내의 함수 사용 가능
        // Call<Root> 반환
        umsService = retrofit.create(IUmsService::class.java)
    }


}