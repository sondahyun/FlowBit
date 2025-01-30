package com.example.flowbit.ui.register

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flowbit.databinding.ActivityRegister1Binding

class RegisterActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityRegister1Binding
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister1Binding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

    private fun sendVerificationCode() {
        // 인증번호 입력 UI 표시
        binding.etVerificationCode.visibility = View.VISIBLE
        binding.tvTimer.visibility = View.VISIBLE
        binding.tvMessage.visibility = View.VISIBLE

        binding.tvMessage.text = "인증번호가 발송되었습니다."
        binding.tvMessage.setTextColor(Color.parseColor("#16A34A"))

        startTimer()
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

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }

    companion object {
        private const val TIMER_DURATION = 10 * 60 * 1000L // 10분
    }
}