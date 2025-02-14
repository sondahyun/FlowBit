package com.example.flowbit.data.repository

import android.util.Log
import com.example.flowbit.data.network.ums.ChangeUserMembershipRequest
import com.example.flowbit.data.network.ums.ChangeUserMembershipResponse
import com.example.flowbit.data.network.ums.RegisterUserRequest
import com.example.flowbit.data.network.ums.RegisterUserResponse
import com.example.flowbit.data.network.ums.UmsService
import com.example.flowbit.data.network.ums.UpdateLostPasswordRequest
import com.example.flowbit.data.network.ums.UpdateLostPasswordResponse
import com.example.flowbit.data.network.ums.VerifyUserEmailRequest
import com.example.flowbit.data.network.ums.VerifyUserEmailResponse

class RegisterRepository(private val umsService: UmsService) {

    suspend fun verifyUserEmailAddress(request: VerifyUserEmailRequest): VerifyUserEmailResponse {
        return try {
            umsService.verifyUserEmailAddress(request) // 정상 요청
        } catch (e: retrofit2.HttpException) {  // HTTP 오류 (401, 402, 403, 404 등)
            Log.e("RegisterRepository", "HTTP 오류 발생: ${e.code()} - ${e.message()}")
            VerifyUserEmailResponse(success = 0, status = e.code(), data = null, msg = "서버 오류 (${e.code()})")
        } catch (e: Exception) { // 네트워크 또는 기타 오류
            Log.e("RegisterRepository", "알 수 없는 오류 발생: ${e.message}")
            VerifyUserEmailResponse(success = 0, status = -1, data = null, msg = "네트워크 오류")
        }
    }

    suspend fun registerUser(request: RegisterUserRequest): RegisterUserResponse {
        return try {
            umsService.registerUser(request) // 정상 요청
        } catch (e: retrofit2.HttpException) {  // HTTP 오류 (401, 402, 403, 404 등)
            Log.e("RegisterRepository", "HTTP 오류 발생: ${e.code()} - ${e.message()}")
            RegisterUserResponse(success = 0, status = e.code(), data = null, msg = "서버 오류 (${e.code()})")
        } catch (e: Exception) { // 네트워크 또는 기타 오류
            Log.e("RegisterRepository", "알 수 없는 오류 발생: ${e.message}")
            RegisterUserResponse(success = 0, status = -1, data = null, msg = "네트워크 오류")
        }
    }

    suspend fun changeUserMembership(request: ChangeUserMembershipRequest): ChangeUserMembershipResponse {
        return try {
            umsService.changeUserMembership(request) // 정상 요청
        } catch (e: retrofit2.HttpException) {  // HTTP 오류 (401, 402, 403, 404 등)
            Log.e("RegisterRepository", "HTTP 오류 발생: ${e.code()} - ${e.message()}")
            ChangeUserMembershipResponse(success = 0, status = e.code(), data = null, msg = "서버 오류 (${e.code()})")
        } catch (e: Exception) { // 네트워크 또는 기타 오류
            Log.e("RegisterRepository", "알 수 없는 오류 발생: ${e.message}")
            ChangeUserMembershipResponse(success = 0, status = -1, data = null, msg = "네트워크 오류")
        }
    }

    suspend fun updateLostPassword(request: UpdateLostPasswordRequest): UpdateLostPasswordResponse {
        return try {
            umsService.updateLostPassword(request) // 정상 요청
        } catch (e: retrofit2.HttpException) {  // HTTP 오류 (401, 402, 403, 404 등)
            Log.e("RegisterRepository", "HTTP 오류 발생: ${e.code()} - ${e.message()}")
            UpdateLostPasswordResponse(success = 0, status = e.code(), data = null, msg = "서버 오류 (${e.code()})")
        } catch (e: Exception) { // 네트워크 또는 기타 오류
            Log.e("RegisterRepository", "알 수 없는 오류 발생: ${e.message}")
            UpdateLostPasswordResponse(success = 0, status = -1, data = null, msg = "네트워크 오류")
        }
    }
}