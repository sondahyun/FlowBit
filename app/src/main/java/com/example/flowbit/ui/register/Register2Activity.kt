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
import com.example.flowbit.FlowBitApplication
import com.example.flowbit.R
import com.example.flowbit.data.network.ums.RegisterUserRequest
import com.example.flowbit.data.network.ums.RegisterUserResponse
import com.example.flowbit.databinding.ActivityRegister2Binding

class Register2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRegister2Binding

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory((application as FlowBitApplication).registerRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() } // 뒤로가기 버튼

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
                val inputCode = s?.toString()?.trim()
                val isValidCode = inputCode?.length == 6 && inputCode.all { it.isDigit() } // 6자리 숫자인지 체크

                Log.d("Register2Activity", "입력된 인증번호: $inputCode, 유효 여부: $isValidCode")

                // 버튼 색상 및 활성화 상태 변경
                binding.btnComplete.isEnabled = isValidCode
                binding.btnComplete.backgroundTintList =
                    getColorStateList(if (isValidCode) R.color.active_button else R.color.inactive_button)
                // 버튼 글자색 변경 (활성화 시 흰색, 비활성화 시 기본 색상)
                binding.btnComplete.setTextColor(
                    if (isValidCode) Color.WHITE else getColor(R.color.inactive_button)
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }



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

    // 비밀번호 조건 검사 (8자 이상 + 특수문자 포함)
    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[!@#\$%^&*_])[A-Za-z0-9!@#\$%^&*_]{8,}$".toRegex()
        return password.matches(regex)
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
        RegisterUserRequest()
        registerViewModel.registerUser()
        Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, Register3Activity::class.java))
        finish()
    }
}