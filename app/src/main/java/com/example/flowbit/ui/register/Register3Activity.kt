package com.example.flowbit.ui.register

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
import com.example.flowbit.data.network.ums.ChangeUserMembershipRequest
import com.example.flowbit.data.network.ums.ChangeUserMembershipResponse
import com.example.flowbit.data.network.ums.RegisterUserRequest
import com.example.flowbit.data.network.ums.RegisterUserResponse
import com.example.flowbit.data.network.ums.UpdateLostPasswordResponse
import com.example.flowbit.databinding.ActivityRegister3Binding
import com.google.android.gms.common.internal.constants.ListAppsActivityContract

class Register3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRegister3Binding
    // 이메일 저장 변수
    private lateinit var userEmail: String
    // 해쉬 비밀번호 저장 변수
    private lateinit var hashedPassword: String
    // app_instance
    private lateinit var appInstance: String
    // guid
    private lateinit var guid: String
    // username
    private lateinit var username: String

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory((application as FlowBitApplication).registerRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // register1activity에서 받아온 email
        userEmail = intent.getStringExtra("email").toString()
        hashedPassword = intent.getStringExtra("hashedPassword").toString()
        appInstance = intent.getStringExtra("app_instance_ID").toString()
        guid = intent.getStringExtra("guid").toString()
        username = intent.getStringExtra("username").toString()

        Log.d("Register3Activity", "$userEmail, $hashedPassword, $appInstance")

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener { finish() }

        // 초기 UI 설정
        binding.btnComplete.isEnabled = false
        binding.btnComplete.setBackgroundColor(Color.parseColor("#E5E7EB"))

        // 입력 완료 버튼 클릭 이벤트
        binding.btnComplete.setOnClickListener {
            validateBeforeSubmit()
        }

        // 숫자 입력하면 색 변함
        // 자동으로 - 삽입
        binding.etPhoneNumber.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false // 포맷팅 중인지 확인하는 플래그
            private var deletingHyphen = false // '-' 삭제 중인지 확인하는 플래그
            private var hyphenPosition = 0 // 삭제된 '-'의 위치 저장

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (count > 0 && s?.get(start) == '-') {
                    deletingHyphen = true
                    hyphenPosition = start
                } else {
                    deletingHyphen = false
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isFormatting || s.isNullOrEmpty()) return

                isFormatting = true
                val cleanText = s.replace(Regex("[^\\d]"), "") // 숫자만 남기기
                val formattedText = formatPhoneNumber(cleanText)
                binding.etPhoneNumber.setText(formattedText)
                binding.etPhoneNumber.setSelection(formattedText.length) // 커서 위치 설정
                isFormatting = false

                val isValidLength = cleanText.length in 10..11 // 10자리 또는 11자리만 허용

                Log.d("Register3Activity", "입력된 휴대폰 번호: $cleanText, 유효 길이: $isValidLength")

                binding.btnComplete.isEnabled = isValidLength
                binding.btnComplete.backgroundTintList =
                    getColorStateList(if (isValidLength) R.color.active_button else R.color.inactive_button)
                binding.btnComplete.setTextColor(
                    if (isValidLength) Color.WHITE else getColor(R.color.inactive_button)
                )
            }

            override fun afterTextChanged(s: Editable?) {}

            // 전화번호 포맷팅 함수
            private fun formatPhoneNumber(number: String): String {
                return when {
                    number.length >= 11 -> "${number.substring(0, 3)}-${number.substring(3, 7)}-${number.substring(7)}"
                    number.length >= 10 -> "${number.substring(0, 3)}-${number.substring(3, 6)}-${number.substring(6)}"
                    else -> number
                }
            }
        })

    }

    private fun validateBeforeSubmit() {
        val phoneNumber = binding.etPhoneNumber.text

        if (phoneNumber == null) {
            showErrorDialog("비밀번호를 입력하세요.")
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


    private fun proceedToNextStep() {
        val phoneNumber = binding.etPhoneNumber.text

        val request = ChangeUserMembershipRequest(guid, membership = 2, phoneNumber.toString(), name = "다현", birthdate = "010517", residence = "SEOUL", payment = "CREDIT")
        Log.d("Register3Activity", "$userEmail, $hashedPassword, $guid, $phoneNumber")
        registerViewModel.changeUserMembership(request)

        registerViewModel.changeUserMembershipResponse.observe(this, Observer { response ->
            handleVerificationResponse(response)
        })
    }

    // 서버 응답 처리
    private fun handleVerificationResponse(response: ChangeUserMembershipResponse?) {
        if (response == null) {
            showErrorDialog("서버 응답이 없습니다. 다시 시도해주세요.")
            return
        }

        when (response.status) {
            201 -> { // 성공
                Log.d("Register3Activity", "회원가입 성공!!!")

                // 다음 화면으로 이동
                Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, SetupPinActivity::class.java).apply {
//                }
                val intent = Intent(this, SetupPasscodeActivity::class.java).apply {
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
        Log.e("Register3Activity", "에러 발생: $message") // 로그 기록
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

}