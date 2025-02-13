package com.example.flowbit.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.flowbit.FlowBitApplication
import com.example.flowbit.R
import com.example.flowbit.data.network.ums.VerifyUserEmailRequest
import com.example.flowbit.data.network.ums.VerifyUserEmailResponse
import com.example.flowbit.databinding.ActivityFindPwd1Binding
import com.example.flowbit.ui.register.RegisterViewModel
import com.example.flowbit.ui.register.RegisterViewModelFactory

class ForgotPassword1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityFindPwd1Binding
    private var countDownTimer: CountDownTimer? = null
    private var verificationCode: Int? = null // 서버에서 받은 인증번호 저장


    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory((application as FlowBitApplication).registerRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPwd1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() } // 뒤로가기 버튼

        // 초기 UI 설정
        resetUI()

        // 인증번호 보내기 버튼 클릭 이벤트
        binding.btnSendCode.setOnClickListener {
            Log.d("ForgotPassword1Activity", "인증번호 보내기 버튼 누름")
            sendVerificationCode()
        }

        // 인증번호 입력 리스너 (6자리 입력하면 자동으로 버튼 활성화)
        binding.etVerificationCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputCode = s?.toString()?.trim()
                val isValidCode = inputCode?.length == 6 && inputCode.all { it.isDigit() } // 6자리 숫자인지 체크

                Log.d("ForgotPasswordActivity", "입력된 인증번호: $inputCode, 유효 여부: $isValidCode")

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
                Log.d("ForgotPasswordActivity", "$verificationCode")

            }
            400 -> showErrorAndReset("입력한 이메일이 올바르지 않습니다. 다시 확인해주세요.")
            401 -> showErrorAndReset("인증 요청이 너무 많습니다. 잠시 후 다시 시도해주세요.")
            402 -> showErrorAndReset("이메일 전송에 실패했습니다. 네트워크 상태를 확인해주세요.")
            403 -> showErrorAndReset("이메일 전송이 차단되었습니다. 관리자에게 문의하세요.")
            else -> showErrorAndReset("알 수 없는 오류가 발생했습니다. 다시 시도해주세요.")
        }
    }

    // 에러 메시지 출력 후 UI 초기화
    private fun showErrorAndReset(message: String) {
        Log.e("ForgotPassword1Activity", "에러 발생: $message") // 로그 기록
//        val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)

        AlertDialog.Builder(this)
            .setTitle("오류")
            .setMessage(message)
            .setPositiveButton("확인") { _, _ -> resetUI() } // 확인 버튼을 누르면 초기화
            .setCancelable(false) // 배경 클릭 방지
            .show()
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
            // email 전달
            val intent = Intent(this, ForgotPassword2Activity::class.java).apply {
                putExtra("email", binding.etEmail.text.toString().trim()) // 이메일 전달
            }
            startActivity(intent)
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