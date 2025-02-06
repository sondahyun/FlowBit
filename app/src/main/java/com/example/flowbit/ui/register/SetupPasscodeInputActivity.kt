package com.example.flowbit.ui.register

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.flowbit.databinding.ActivitySetupPasscodeInputBinding

class SetupPasscodeInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupPasscodeInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupPasscodeInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 패스코드 입력 이벤트 처리
        binding.etPasscode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val isNotEmpty = !s.isNullOrBlank()
                binding.btnComplete.isEnabled = isNotEmpty
                binding.btnComplete.setTextColor(if (isNotEmpty) 0xFFFFFFFF.toInt() else 0xFF94A3B8.toInt())
                binding.btnComplete.setBackgroundColor(if (isNotEmpty) 0xFF60A5FA.toInt() else 0xFFE5E7EB.toInt())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // "완료" 버튼 클릭 시 다이얼로그 표시
        binding.btnComplete.setOnClickListener {
            showPasscodeConfirmationDialog()
        }

        // 뒤로 가기 버튼 이벤트
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    // 패스코드 확인 다이얼로그 표시
    private fun showPasscodeConfirmationDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("⚠ 입력한 패스코드를 기억하셨나요?")
            .setMessage("패스코드는 소유물을 추가하고 전송할 때, 매번 입력하는 암호입니다.")
            .setPositiveButton("확인") { _, _ ->
                // 다음 화면으로 이동하거나 저장 처리
            }
            .setNegativeButton("취소", null)
            .create()

        dialog.show()
    }
}