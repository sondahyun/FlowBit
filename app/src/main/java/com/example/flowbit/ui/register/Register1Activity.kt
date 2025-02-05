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
import androidx.lifecycle.lifecycleScope
import com.example.flowbit.FlowBitApplication
import com.example.flowbit.data.network.ums.VerifyUserEmailRequest
import com.example.flowbit.data.network.ums.VerifyUserEmailResponse
import com.example.flowbit.databinding.ActivityRegister1Binding
import com.example.flowbit.ui.expense.ExpenseViewModel
import com.example.flowbit.ui.expense.ExpenseViewModelFactory
import com.example.flowbit.ui.login.LoginActivity
import kotlinx.coroutines.launch

class Register1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRegister1Binding
    private var countDownTimer: CountDownTimer? = null

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory((application as FlowBitApplication).registerRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish() // 현재 액티비티 종료 후 이전 화면으로 돌아감
        }


        // 초기 버튼 비활성화 및 UI 숨김 처리
        binding.btnComplete.isEnabled = false
        binding.btnComplete.setBackgroundColor(Color.parseColor("#E5E7EB"))
        binding.etVerificationCode.visibility = View.GONE
        binding.tvTimer.visibility = View.GONE
        binding.tvMessage.visibility = View.GONE

        // 인증번호 보내기 버튼 클릭 이벤트
        binding.btnSendCode.setOnClickListener {
            sendVerificationCode()
        }

        // 인증번호 입력 필드 TextWatcher 추가
        // TextWatcher 인터페이스 이용
        // beforeTextChanged(): 입력 내용이 바뀌기 직전 호출
        // onTextChanged(): 입력 내용이 바뀔때마다 호출
        // afterTextChanged(): 입력 완료된 후 호출
        binding.etVerificationCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnComplete.isEnabled = !s.isNullOrEmpty() // isNullOrEmpty: 입력값이 null 이거나 **빈 문자열(””)**이면 true 반환
                binding.btnComplete.setBackgroundColor(
                    if (!s.isNullOrEmpty()) Color.parseColor("#2563EB") else Color.parseColor("#E5E7EB")
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })

//        // LiveData 관찰 (API 응답 처리)
//        registerViewModel.responseVerifyUserEmailResponse.observe(this) { response ->
//            handleVerificationResponse(response)
//        }
    }

    private fun sendVerificationCode() {
        val email = binding.etEmail.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val request = VerifyUserEmailRequest(email, "test")
        registerViewModel.verifyUserEmailAddress(request)



    }

    private fun handleVerificationResponse(response: VerifyUserEmailResponse) {
        // ✅ response가 null일 경우 기본 처리
        if (response == null) {
            showErrorDialog("서버 응답이 없습니다. 다시 시도해주세요.")
            return
        }

        when (response.status) {
            201 -> { // ✅ 성공
                binding.etVerificationCode.visibility = View.VISIBLE
                binding.tvTimer.visibility = View.VISIBLE
                binding.tvMessage.visibility = View.VISIBLE
                binding.tvMessage.text = "인증번호가 발송되었습니다."
                binding.tvMessage.setTextColor(Color.parseColor("#16A34A"))
                startTimer()
            }
            400, 401, 402 -> { // 🔹 일반적인 오류
                showErrorDialog("이메일 전송에 실패했습니다. 다시 입력해주세요.")
            }
            403 -> { // 🔹 권한 문제
                showErrorDialog("이메일 전송이 차단되었습니다. 관리자에게 문의하세요.")
            }
            else -> { // ✅ 기타 오류 처리
                showErrorDialog("알 수 없는 오류가 발생했습니다. 다시 시도해주세요.")
            }
        }
    }

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