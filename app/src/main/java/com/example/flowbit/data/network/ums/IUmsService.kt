package com.example.flowbit.data.network.ums

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IUmsService {

    @GET("checkUserEmailExists")
    suspend fun checkUserEmailExists(@Query("email_address") email: String): EmailCheckResponse

    @POST("registerUser")
    suspend fun registerUser(@Body request: RegisterUserRequest): RegisterUserResponse

    @POST("loginUser")
    suspend fun loginUser(@Body request: LoginUserRequest): LoginUserResponse

    @POST("loginSocialUser")
    suspend fun loginSocialUser(@Body request: LoginSocialUserRequest): LoginSocialUserResponse

    @GET("getUserInfo")
    suspend fun getUserInfo(@Query("GUID") guid: String): UserInfoResponse

    @POST("updateFCMToken")
    suspend fun updateFCMToken(@Body request: UpdateFCMTokenRequest): UpdateFCMTokenResponse

    @POST("changeUserMembership")
    suspend fun changeUserMembership(@Body request: ChangeUserMembershipRequest): ChangeUserMembershipResponse

    @POST("unregisterUser")
    suspend fun unregisterUser(@Body request: UnregisterUserRequest): UnregisterUserResponse

    @POST("updateUsername")
    suspend fun updateUsername(@Body request: UpdateUsernameRequest): UpdateUsernameResponse

    @POST("updateLostPassword")
    suspend fun updateLostPassword(@Body request: UpdateLostPasswordRequest): UpdateLostPasswordResponse

    @POST("updatePassword")
    suspend fun updatePassword(@Body request: UpdatePasswordRequest): UpdatePasswordResponse

    @POST("verifyUserEmailAddress")
    suspend fun verifyUserEmailAddress(@Body request: VerifyUserEmailRequest): VerifyUserEmailResponse

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