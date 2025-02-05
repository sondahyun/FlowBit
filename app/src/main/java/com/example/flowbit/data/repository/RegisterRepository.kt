package com.example.flowbit.data.repository

import com.example.flowbit.data.network.ums.UmsService
import com.example.flowbit.data.network.ums.VerifyUserEmailRequest
import com.example.flowbit.data.network.ums.VerifyUserEmailResponse

class RegisterRepository(private val umsService: UmsService) {

    suspend fun verifyUserEmailAddress(request: VerifyUserEmailRequest): VerifyUserEmailResponse {
        return umsService.verifyUserEmailAddress(request)
    }

}