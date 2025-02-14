package com.example.flowbit.data.network.ums

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IUmsService {

    @GET("checkUserEmailExists")
    suspend fun checkUserEmailExists(@Query("email_address") email: String): EmailCheckResponse

    @POST("loginUser")
    suspend fun loginUser(@Body request: LoginUserRequest): LoginUserResponse

    @POST("loginSocialUser")
    suspend fun loginSocialUser(@Body request: LoginSocialUserRequest): LoginSocialUserResponse

    @GET("getUserInfo")
    suspend fun getUserInfo(@Query("GUID") guid: String): UserInfoResponse

    @POST("updateFCMToken")
    suspend fun updateFCMToken(@Body request: UpdateFCMTokenRequest): UpdateFCMTokenResponse

    @POST("unregisterUser")
    suspend fun unregisterUser(@Body request: UnregisterUserRequest): UnregisterUserResponse

    @POST("updateUsername")
    suspend fun updateUsername(@Body request: UpdateUsernameRequest): UpdateUsernameResponse

    @POST("updatePassword")
    suspend fun updatePassword(@Body request: UpdatePasswordRequest): UpdatePasswordResponse

    @POST("verifyUserEmailAddress")
    suspend fun verifyUserEmailAddress(@Body request: VerifyUserEmailRequest): VerifyUserEmailResponse

    @POST("registerUser") // ID/Password 회원으로 회원가입. 유저 정보 저장 시 생성일/수정일 저장
    suspend fun registerUser(@Body request: RegisterUserRequest): RegisterUserResponse

    @POST("changeUserMembership") // 유저 회원가입 후 사용자에게 개인정보 내용 입력받아 서버 DB 업데이트를 위해 호출되는 API
    suspend fun changeUserMembership(@Body request: ChangeUserMembershipRequest): ChangeUserMembershipResponse

    @POST("updateLostPassword") // 로그인 전 비밀번호 찾기
    suspend fun updateLostPassword(@Body request: UpdateLostPasswordRequest): UpdateLostPasswordResponse

    @POST("getAllUMSNotice")
    suspend fun getAllUMSNotice(@Body request: GetUMSNoticeRequest): GetUMSNoticeResponse

    @POST("uploadCredentialFile")
    suspend fun uploadCredentialFile(@Body request: UploadCredentialFileRequest): UploadCredentialFileResponse

    @POST("downloadCredentialFile")
    suspend fun downloadCredentialFile(@Body request: DownloadCredentialFileRequest): DownloadCredentialFileResponse

    @GET("loginTime")
    suspend fun getLoginTime(@Query("GUID") guid: String): LoginTimeResponse

    @GET("getGUIDFromEmail")
    suspend fun getGUIDFromEmail(@Query("email") email: String): GUIDResponse
}