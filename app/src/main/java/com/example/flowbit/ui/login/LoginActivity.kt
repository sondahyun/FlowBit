package com.example.flowbit.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flowbit.MainActivity
import com.example.flowbit.databinding.ActivityLoginBinding
import com.example.flowbit.ui.register.RegisterActivity1

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.btnStart.isEnabled = false
        binding.btnStart.setBackgroundColor(Color.parseColor("#E5E7EB"))

        // 입력 필드 리스너 설정 (이메일 & 비밀번호 입력 감지)
        binding.etEmail.addTextChangedListener(loginTextWatcher)
        binding.etPassword.addTextChangedListener(loginTextWatcher)

        // 로그인 버튼 클릭 이벤트
        binding.btnStart.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            }
        }

        // 회원가입 버튼 클릭 이벤트
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity1::class.java))
        }

        // 비밀번호 찾기 버튼 클릭 이벤트
        binding.tvFindPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    // 이메일과 비밀번호 입력 감지하여 버튼 활성화
    private val loginTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            val isValid = email.isNotEmpty() && password.isNotEmpty()
            binding.btnStart.isEnabled = isValid
            binding.btnStart.setBackgroundColor(if (isValid) Color.parseColor("#2563EB") else Color.parseColor("#E5E7EB"))
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    // 로그인 요청 처리
    private fun loginUser(email: String, password: String) {
        // TODO: 실제 로그인 API 연동 필요
        if (email == "test@flowbit.com" && password == "password123") {
            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "이메일 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}