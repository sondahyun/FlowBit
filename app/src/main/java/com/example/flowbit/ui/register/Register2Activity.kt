package com.example.flowbit.ui.register

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.flowbit.FlowBitApplication
import com.example.flowbit.R
import com.example.flowbit.data.network.ums.GUIDResponse
import com.example.flowbit.data.network.ums.RegisterUserRequest
import com.example.flowbit.data.network.ums.RegisterUserResponse
import com.example.flowbit.data.network.ums.VerifyUserEmailResponse
import com.example.flowbit.databinding.ActivityRegister2Binding
import java.security.MessageDigest

class Register2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRegister2Binding
    // 이메일 저장 변수
    private lateinit var userEmail: String
    // response (registerUser API)
    private var guid: String? = null
    private var username: String? = null
    // hashedPassword
    private lateinit var hashedPassword: String

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory((application as FlowBitApplication).registerRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // register1activity에서 받아온 email
        userEmail = intent.getStringExtra("email").toString()
        Log.d("Register2Activity", "받은 이메일: $userEmail")

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener { finish() }

        // 초기 UI 설정
        binding.btnComplete.isEnabled = false
        binding.btnComplete.setBackgroundColor(Color.parseColor("#E5E7EB"))
        binding.tvErrorMessage.visibility = View.GONE

        // 비밀 번호 보기 버튼 설정
        binding.btnTogglePasswordVisibility.setOnClickListener { togglePasswordVisibility(binding.etNewPassword) }
        binding.btnToggleConfirmPasswordVisibility.setOnClickListener { togglePasswordVisibility(binding.etConfirmPassword) }

        // 입력 완료 버튼 클릭 이벤트
        binding.btnComplete.setOnClickListener {
            validateBeforeSubmit()
        }

        // 글자 입력하면 색 변함
        // 인증번호 입력 리스너 (6자리 입력하면 자동으로 버튼 활성화)
        binding.etNewPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputPassword = s?.toString()?.trim()
                val isValidPassword = isValidPassword(inputPassword)

                Log.d("Register2Activity", "입력된 비밀번호: $inputPassword, 유효 여부: $isValidPassword")

                // 버튼 색상 및 활성화 상태 변경
                binding.btnComplete.isEnabled = isValidPassword
                binding.btnComplete.backgroundTintList =
                    getColorStateList(if (isValidPassword) R.color.active_button else R.color.inactive_button)

                // 버튼 글자색 변경 (활성화 시 흰색, 비활성화 시 기본 색상)
                binding.btnComplete.setTextColor(
                    if (isValidPassword) Color.WHITE else getColor(R.color.inactive_button)
                )
                if (isValidPassword) {
                    Log.d("Register2Activity", "비밀번호 8자 이상, 특수문자 1개 이상: 조건 만족함")
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }
    // 비밀번호 유효성 검사 함수
    private fun isValidPassword(password: String?): Boolean {
        if (password.isNullOrEmpty()) return false
        val regex = "^(?=.*[!@#\$%^&*_])[A-Za-z0-9!@#\$%^&*_]{8,}$".toRegex()
        return password.matches(regex)
    }


//    // 비밀번호 조건 검사 (8자 이상 + 특수문자 포함)
//    private fun isValidPassword(password: String): Boolean {
//        val regex = "^(?=.*[!@#\$%^&*_])[A-Za-z0-9!@#\$%^&*_]{8,}$".toRegex()
//        return password.matches(regex)
//    }

    // **입력 완료 버튼 클릭 시 검증**
    private fun validateBeforeSubmit() {
        val newPassword = binding.etNewPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (newPassword == null) {
            showErrorDialog("비밀번호를 입력하세요.")
            return
        }

        if (confirmPassword == null) {
            showErrorDialog("비밀번호 확인을 입력하세요.")
            return
        }

        // 1. 비밀번호 조건 검사 (8자 이상 + 특수문자 1개 포함)
        if (!isValidPassword(newPassword)) {
            showPasswordAlert()
            return
        }

        // 2. 비밀번호 일치 여부 검사
        if (newPassword != confirmPassword) {
            binding.tvErrorMessage.visibility = View.VISIBLE
            return
        }

        // 모든 조건 만족 -> 다음 단계 (예: 회원가입 완료)
        proceedToNextStep()
    }

    private fun showErrorDialog(message: String) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("오류")
            .setMessage(message)
            .setPositiveButton("확인", null)
            .show()
    }

    // 비밀번호 조건 불충족 시 경고창 표시
    private fun showPasswordAlert() {
        AlertDialog.Builder(this)
            .setTitle("비밀번호 오류")
            .setMessage("비밀번호 조건을 다시 확인해주세요.\n(8자 이상, 특수문자 1개 포함)")
            .setPositiveButton("확인", null)
            .show()
    }

    // 비밀번호 가시성 토글 (보이기/숨기기)
    private fun togglePasswordVisibility(editText: android.widget.EditText) {
        val isPasswordVisible = editText.inputType == android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        editText.inputType = if (isPasswordVisible) {
            android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        editText.setSelection(editText.text.length) // 커서 위치 유지
    }

    // 회원가입 완료
    private fun proceedToNextStep() {
        val newPassword = binding.etNewPassword.text.toString()


        // 비밀번호를 SHA-256으로 해시
        hashedPassword = hashPassword(newPassword)

        // 해시된 비밀번호를 API에 전달
        val request = RegisterUserRequest(userEmail, hashedPassword, "test")
        Log.d("Register2Activity", "$userEmail, $hashedPassword, test")
        registerViewModel.registerUser(request)

        // 서버 응답을 감지하여 처리
        registerViewModel.responseRegisterUserResponse.observe(this, Observer { response ->
            handleVerificationResponse(response)

//            response.data?.let { data ->
//                guid = data.guid
//                username = data.username
//
//                // 다음 화면으로 이동
//                val intent = Intent(this, Register3Activity::class.java).apply {
//                    putExtra("email", userEmail)
//                    putExtra("hashedPassword", hashedPassword)
//                    putExtra("app_instance_ID", "test")
//                    putExtra("guid", guid)
//                    putExtra("username", username)
//                }
//                startActivity(intent)
//                finish()
//            } ?: run {
//                showErrorDialog("회원가입에 실패하였습니다. 다시 시도해주세요.")
//            }
        })
    }

    // 서버 응답 처리
    private fun handleVerificationResponse(response: RegisterUserResponse?) {
        if (response == null) {
            showErrorDialog("서버 응답이 없습니다. 다시 시도해주세요.")
            return
        }

        when (response.status) {
            201 -> { // 성공
                guid = response.data?.guid  // 서버에서 받은 guid 저장
                username = response.data?.username // 서버에서 받은 username
                Log.d("Register2Activity", "$guid")

                // 다음 화면으로 이동
                val intent = Intent(this, Register3Activity::class.java).apply {
                    putExtra("email", userEmail)
                    putExtra("hashedPassword", hashedPassword)
                    putExtra("app_instance_ID", "test")
                    putExtra("guid", guid)
                    putExtra("username", username)
                }
                startActivity(intent)
                finish()

            }
            400 -> showErrorAndReset("Null Exception Error")
            401 -> showErrorAndReset("파라미터 누락되었습니다.")
            402 -> showErrorAndReset("알 수 없는 에러가 발생했습니다.")
            403 -> showErrorAndReset("해당 정보를 가지고 있는 사용자가 이미 있습니다.")
            404 -> showErrorAndReset("정보 저장 시 에러가 발생했습니다.")
            405 -> showErrorAndReset("탈퇴한 아이디로 로그인을 시도했습니다.")
            else -> showErrorAndReset("")
        }
    }

    // 에러 메시지 출력 후 UI 초기화
    private fun showErrorAndReset(message: String) {
        Log.e("Register2Activity", "에러 발생: $message") // 로그 기록
//        val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("오류")
            .setMessage(message)
            .setPositiveButton("확인") { _, _ -> resetUI() } // 확인 버튼을 누르면 초기화
            .setCancelable(false) // 배경 클릭 방지
            .show()
    }

    private fun resetUI() {
        binding.btnComplete.isEnabled = false
        binding.btnComplete.setBackgroundColor(Color.parseColor("#E5E7EB"))
    }

    // SHA-256 해시 함수 추가
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256") // SHA-256 알고리즘 사용
        val digest = md.digest(bytes) // 해싱 수행
        return digest.joinToString("") { "%02x".format(it) } // 16진수 문자열로 변환
    }
}