package com.example.flowbit.ui.register

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flowbit.databinding.ActivityRegister2Binding

class RegisterActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityRegister2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 UI 설정
        binding.btnComplete.isEnabled = false
        binding.btnComplete.setBackgroundColor(Color.parseColor("#E5E7EB"))
        binding.tvErrorMessage.visibility = View.GONE

        // 비밀번호 입력 리스너 추가
        binding.etNewPassword.addTextChangedListener(passwordWatcher)
        binding.etConfirmPassword.addTextChangedListener(passwordWatcher)

        // 비밀번호 보기 버튼 설정
        binding.btnTogglePasswordVisibility.setOnClickListener { togglePasswordVisibility(binding.etNewPassword) }
        binding.btnToggleConfirmPasswordVisibility.setOnClickListener { togglePasswordVisibility(binding.etConfirmPassword) }

        // 입력 완료 버튼 클릭 이벤트
        binding.btnComplete.setOnClickListener {
            validateBeforeSubmit()
        }
    }

    // 비밀번호 검증 로직 (실시간 체크)
    private val passwordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validatePassword()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    // **입력 완료 버튼 클릭 시 검증**
    private fun validateBeforeSubmit() {
        val newPassword = binding.etNewPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

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

    private fun validatePassword() {
        val newPassword = binding.etNewPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        // 비밀번호 조건 만족 여부 확인
        if (!isValidPassword(newPassword)) {
            binding.btnComplete.isEnabled = false
            binding.btnComplete.setBackgroundColor(Color.parseColor("#E5E7EB"))
            return
        }

        // 비밀번호 일치 여부 검사
        if (newPassword == confirmPassword) {
            binding.tvErrorMessage.visibility = View.GONE
            binding.btnComplete.isEnabled = true
            binding.btnComplete.setBackgroundColor(Color.parseColor("#2563EB")) // 파란색 활성화
        } else {
            binding.tvErrorMessage.visibility = View.VISIBLE
            binding.btnComplete.isEnabled = false
            binding.btnComplete.setBackgroundColor(Color.parseColor("#E5E7EB")) // 회색 비활성화
        }
    }

    // 비밀번호 조건 검사 (8자 이상 + 특수문자 포함)
    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[!@#\$%^&*()_+=]).{8,}$".toRegex()
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

    // 다음 단계 진행 (이 부분은 실제 앱 로직에 맞게 구현)
    private fun proceedToNextStep() {
        AlertDialog.Builder(this)
            .setTitle("회원가입 완료")
            .setMessage("비밀번호가 설정되었습니다.")
            .setPositiveButton("확인") { _, _ ->
                finish() // 현재 액티비티 종료 후 이전 화면으로 이동
            }
            .show()
    }
}