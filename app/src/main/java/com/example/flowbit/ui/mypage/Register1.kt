import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flowbit.databinding.ActivityMainBinding
import com.example.flowbit.databinding.Register1Binding

class Register1 : AppCompatActivity() {
    private var countDownTimer: CountDownTimer? = null

    private val binding: Register1Binding by lazy {
        Register1Binding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // 초기 버튼 상태 설정
        binding.btnNext.setEnabled(false)
        binding.btnNext.setBackgroundColor(Color.parseColor("#E5E7EB"))

        // 인증번호 보내기 버튼 클릭 이벤트
        binding.btnSendCode.setOnClickListener { v ->
            sendVerificationCode()
        }

        // 인증번호 입력 필드 TextWatcher 추가
        binding.etVerificationCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // 입력된 텍스트가 있으면 버튼 활성화
                if (s.length > 0) {
                    binding.btnNext.setEnabled(true)
                    binding.btnNext.setBackgroundColor(Color.parseColor("#2563EB")) // 파란색
                } else {
                    binding.btnNext.setEnabled(false)
                    binding.btnNext.setBackgroundColor(Color.parseColor("#E5E7EB")) // 회색
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }
        })
    }

    private fun sendVerificationCode() {
        // 인증번호 발송 로직
        binding.etVerificationCode.setVisibility(View.VISIBLE)
        binding.tvTimer.setVisibility(View.VISIBLE)
        binding.tvMessage.setVisibility(View.VISIBLE)

        binding.tvMessage.setText("인증번호가 발송되었습니다.")
        startTimer()
    }

    private fun startTimer() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }

        countDownTimer = object : CountDownTimer(TIMER_DURATION, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished / 1000) % 60
                binding.tvTimer.setText(String.format("%02d:%02d", minutes, seconds))
            }

            override fun onFinish() {
                binding.tvTimer.setText("시간 초과")
                binding.tvMessage.setText("인증번호가 만료되었습니다.")
            }
        }
        (countDownTimer as CountDownTimer).start()
    }

    override fun onDestroy() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        super.onDestroy()
    }

    companion object {
        private const val TIMER_DURATION = (10 * 60 * 1000 // 10 minutes in milliseconds
                ).toLong()
    }
}