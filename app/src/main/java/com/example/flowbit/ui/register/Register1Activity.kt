package com.example.flowbit.ui.register

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.flowbit.FlowBitApplication
import com.example.flowbit.data.network.ums.VerifyUserEmailRequest
import com.example.flowbit.data.network.ums.VerifyUserEmailResponse
import com.example.flowbit.databinding.ActivityRegister1Binding

class Register1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRegister1Binding
    private var countDownTimer: CountDownTimer? = null
    private var verificationCode: Int? = null // 서버에서 받은 인증번호 저장

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory((application as FlowBitApplication).registerRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() } // 뒤로가기 버튼

        // 초기 UI 설정
        resetUI()

        // 인증번호 보내기 버튼 클릭 이벤트
        binding.btnSendCode.setOnClickListener { sendVerificationCode() }

        // 인증번호 입력 리스너 (6자리 입력하면 자동으로 버튼 활성화)
        binding.etVerificationCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputCode = s?.toString()?.trim()
                val isValidCode = inputCode?.length == 6
                binding.btnComplete.isEnabled = isValidCode
                binding.btnComplete.setBackgroundColor(
                    if (isValidCode) Color.parseColor("#2563EB") else Color.parseColor("#E5E7EB")
                )
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 입력 완료 버튼 클릭 이벤트 (인증번호 확인)
        binding.btnComplete.setOnClickListener { verifyCode() }
    }

    private fun resetUI() {
        binding.btnComplete.isEnabled = false
        binding.btnComplete.setBackgroundColor(Color.parseColor("#E5E7EB"))
        binding.etVerificationCode.visibility = View.GONE
        binding.tvTimer.visibility = View.GONE
        binding.tvMessage.visibility = View.GONE
    }

    // 인증번호 요청
    private fun sendVerificationCode() {
        val email = binding.etEmail.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val request = VerifyUserEmailRequest(email, "test")
        registerViewModel.verifyUserEmailAddress(request)

        // 서버 응답을 감지하여 처리
        registerViewModel.responseVerifyUserEmailResponse.observe(this, Observer { response ->
            handleVerificationResponse(response)
        })

        binding.etVerificationCode.visibility = View.VISIBLE
        binding.tvTimer.visibility = View.VISIBLE
        binding.tvMessage.visibility = View.VISIBLE
        binding.tvMessage.text = "인증번호가 발송되었습니다."
        binding.tvMessage.setTextColor(Color.parseColor("#16A34A"))
        startTimer()
    }

    // 서버 응답 처리
    private fun handleVerificationResponse(response: VerifyUserEmailResponse?) {
        if (response == null) {
            showErrorDialog("서버 응답이 없습니다. 다시 시도해주세요.")
            return
        }

        when (response.status) {
            201 -> { // 성공
                verificationCode = response.data?.code // 서버에서 받은 인증번호 저장
            }
            400, 401, 402 -> showErrorDialog("이메일 전송에 실패했습니다. 다시 입력해주세요.")
            403 -> showErrorDialog("이메일 전송이 차단되었습니다. 관리자에게 문의하세요.")
            else -> showErrorDialog("알 수 없는 오류가 발생했습니다. 다시 시도해주세요.")
        }
    }

    // 인증번호 확인
    private fun verifyCode() {
        val inputCode = binding.etVerificationCode.text.toString().toIntOrNull()

        if (inputCode == null) {
            showErrorDialog("올바른 인증번호를 입력해주세요.")
            return
        }

        if (inputCode == verificationCode) {
            Toast.makeText(this, "인증 성공!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Register2Activity::class.java))
            finish()
        } else {
            showErrorDialog("인증번호가 일치하지 않습니다.")
        }
    }

    // 타이머 시작 (10분)
    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(TIMER_DURATION, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished / 1000) % 60
                binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.tvTimer.text = "시간 초과"
                binding.tvMessage.text = "인증번호가 만료되었습니다."
                binding.tvMessage.setTextColor(Color.RED)
            }
        }.start()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("오류")
            .setMessage(message)
            .setPositiveButton("확인", null)
            .show()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }

    companion object {
        private const val TIMER_DURATION = 10 * 60 * 1000L // 10분
    }
}