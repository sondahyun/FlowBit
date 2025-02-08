package com.example.flowbit.data.network.ums

import com.google.gson.annotations.SerializedName

// 이메일 존재 여부 확인 API (checkUserEmailExists)
data class EmailCheckResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: Boolean, // 이메일 존재 여부
    @SerializedName("msg") val msg: String
)

// 일반 로그인 API (loginUser)
data class LoginUserRequest(
    val email_address: String,
    val password_hash: String,
    val app_instance_ID: String
)

data class LoginUserResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("GUID") val guid: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("email_address") val email: String?,
    @SerializedName("membership") val membership: Int?,
    @SerializedName("msg") val msg: String
)

// 소셜 로그인 API (loginSocialUser)
data class LoginSocialUserRequest(
    val jwt_token: String,
    val app_instance_ID: String,
    val login_type: Int
)

data class LoginSocialUserResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("GUID") val guid: String?,
    @SerializedName("email_address") val email: String?,
    @SerializedName("membership") val membership: Int?,
    @SerializedName("msg") val msg: String
)

// 사용자 정보 조회 API (getUserInfo)
data class UserInfoResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("GUID") val guid: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("email_address") val email: String?,
    @SerializedName("membership") val membership: Int?,
    @SerializedName("msg") val msg: String
)

// FCM 토큰 업데이트 API (updateFCMToken)
data class UpdateFCMTokenRequest(
    val GUID: String,
    val fcm_token: String
)

data class UpdateFCMTokenResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("msg") val msg: String
)


// 회원 탈퇴 API (unregisterUser)
data class UnregisterUserRequest(
    val GUID: String
)

data class UnregisterUserResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("msg") val msg: String
)

// 사용자 이름 변경 API (updateUsername)
data class UpdateUsernameRequest(
    val GUID: String,
    val username: String,
    val login_type: Int
)

data class UpdateUsernameResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("msg") val msg: String
)

// 비밀번호 분실 후 업데이트 API (updateLostPassword)
data class UpdateLostPasswordRequest(
    val email_address: String,
    val password_hash: String
)

data class UpdateLostPasswordResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("msg") val msg: String
)

// 비밀번호 변경 API (updatePassword)
data class UpdatePasswordRequest(
    val GUID: String,
    val password_hash: String
)

data class UpdatePasswordResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("msg") val msg: String
)

// 이메일 인증 API (verifyUserEmailAddress)
data class VerifyUserEmailRequest(
    val email_address: String,
    val app_instance_ID: String
)

data class VerifyUserEmailResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: EmailVerificationData?,
    @SerializedName("msg") val msg: String
)

// `data` 필드 정의
data class EmailVerificationData(
    @SerializedName("code") val code: Int,
    @SerializedName("transfer_time") val transferTime: Long
)

// 회원가입 API (registerUser)
data class RegisterUserRequest(
    val email_address: String,
    val password_hash: String,
    val app_instance_ID: String
)

data class RegisterUserResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: RegisterUserData?, // "data" 필드 추가
    @SerializedName("msg") val msg: String
)

// "data" 필드에 대한 데이터 클래스 추가
data class RegisterUserData(
    @SerializedName("GUID") val guid: String,
    @SerializedName("username") val username: String
)

// 회원 등급 변경 API (changeUserMembership)
data class ChangeUserMembershipRequest(
    val GUID: String,
    val membership: Int,
    val phone_number: String,
    val name: String?,
    val birthdate: String?,
    val residence: String?,
    val payment: String?
)

data class ChangeUserMembershipResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: Any?,
    @SerializedName("msg") val msg: String
)

// 공지사항 조회 API (getAllUMSNotice)
data class GetUMSNoticeRequest(
    val pageNum: Int,
    val pageSize: Int
)

data class GetUMSNoticeResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("msg") val msg: String
)

// GUID 조회 API (getGUIDFromEmail)
data class GUIDResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("GUID") val guid: String?,
    @SerializedName("msg") val msg: String
)

// 로그인 시간 조회 API (loginTime)
data class LoginTimeResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("login_at") val loginAt: Long?,
    @SerializedName("msg") val msg: String
)


// 자격 증명 파일 업로드 API (uploadCredentialFile)
data class UploadCredentialFileRequest(
    val GUID: String,
    val file_data: String, // Base64 인코딩된 파일 데이터
    val file_name: String
)

data class UploadCredentialFileResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("msg") val msg: String
)

// 자격 증명 파일 다운로드 API (downloadCredentialFile)
data class DownloadCredentialFileRequest(
    val GUID: String,
    val file_name: String
)

data class DownloadCredentialFileResponse(
    @SerializedName("success") val success: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("file_data") val fileData: String?, // Base64 인코딩된 파일 데이터
    @SerializedName("msg") val msg: String
)