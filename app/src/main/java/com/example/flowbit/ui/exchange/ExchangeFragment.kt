package com.example.flowbit.ui.exchange

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flowbit.FlowBitApplication
import com.example.flowbit.R
import com.example.flowbit.databinding.FragmentExchangeBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MoneyFragment : Fragment() {
    private lateinit var binding: FragmentExchangeBinding // 뷰 바인딩 추가
    private lateinit var exchangeAdapter: ExchangeAdapter

    private val exchangeViewModel: ExchangeViewModel by viewModels {
        ExchangeViewModelFactory((requireActivity().application as FlowBitApplication).exchangeRepository)
    }

    // 기본값: 오늘 날짜, API 형식에 맞춰 `yyyyMMdd`로 설정
    private var selectedDate: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExchangeBinding.inflate(inflater, container, false) // 뷰 바인딩 초기화
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        binding.exchangeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        exchangeAdapter = ExchangeAdapter(emptyList())
        binding.exchangeRecyclerView.adapter = exchangeAdapter

        // 환율 정보 가져오기
        fetchExchangeRates()
    }


    private fun fetchExchangeRates() {
        Log.d("ExchangeFragment", "크립토 환율 정보 요청")

        exchangeViewModel.getExchanges()

        // LiveData 관찰
        exchangeViewModel.exchanges.observe(viewLifecycleOwner) { exchanges ->
            if (exchanges != null && exchanges.isNotEmpty()) {
                Log.d("ExchangeFragment", "크립토 환율 정보 불러오기 성공: ${exchanges.size}개의 데이터")
                // RecyclerView 업데이트
                exchangeAdapter.updateData(exchanges)
                binding.exchangeRecyclerView.visibility = View.VISIBLE
            } else {
                Log.e("ExchangeFragment", "크립토 환율 정보를 불러올 수 없습니다.")
                Toast.makeText(requireContext(), "크립토 환율 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                binding.exchangeRecyclerView.visibility = View.GONE
            }
        }
    }
}