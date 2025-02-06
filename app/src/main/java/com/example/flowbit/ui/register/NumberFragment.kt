package com.example.flowbit.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flowbit.databinding.FragmentNumberPadBinding

class NumberFragment : Fragment() {
    private var _binding: FragmentNumberPadBinding? = null
    private val binding get() = _binding!!

    // 숫자 버튼 클릭 시 실행할 함수
    private var onNumberClick: ((String) -> Unit)? = null
    // 삭제 버튼 클릭 시 실행할 함수
    private var onDeleteClick: (() -> Unit)? = null

    fun setOnNumberClickListener(listener: (String) -> Unit) {
        onNumberClick = listener
    }

    fun setOnDeleteClickListener(listener: () -> Unit) {
        onDeleteClick = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNumberPadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 숫자 버튼 리스트
        val numberButtons = listOf(
            binding.btn0,
            binding.btn1,
            binding.btn2,
            binding.btn3,
            binding.btn4,
            binding.btn5,
            binding.btn6,
            binding.btn7,
            binding.btn8,
            binding.btn9
        )

        // 숫자 버튼 클릭 이벤트 설정
        numberButtons.forEach { button ->
            button.setOnClickListener {
                onNumberClick?.invoke(button.text.toString())
            }
        }

        // 삭제 버튼 클릭 이벤트 설정
        binding.btnDelete.setOnClickListener {
            onDeleteClick?.invoke()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // View Binding 해제 (메모리 누수 방지)
    }
}