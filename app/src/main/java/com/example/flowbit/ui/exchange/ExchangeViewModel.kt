package com.example.flowbit.ui.exchange

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowbit.data.network.bsp.Exchange
import com.example.flowbit.data.repository.ExchangeRepository
import kotlinx.coroutines.launch


class ExchangeViewModel (val exchangeRepository: ExchangeRepository) : ViewModel() {

    // DB 관련 참고 부분
    /*
    // Flow 를 사용하여 지속 관찰
    val allRefs : LiveData<List<RefEntity>> = refRepository.allRefs.asLiveData()

    // one-shot 결과를 확인하고자 할 때 사용
    private var _name = MutableLiveData<String>()
    val nameData = _name

    // viewModelScope 는 Dispatcher.Main 이므로 긴시간이 걸리는 IO 작업은 Dispatchers.IO 에서 작업
    fun findName(id: Int) = viewModelScope.launch {
        var result : String
        withContext(Dispatchers.IO) {
            result = refRepository.getNameById(id)
        }
        _name.value = result
    }
    */

    private var _exchanges = MutableLiveData<List<Exchange>?>()
    val exchanges = _exchanges

    // 유니와플에서 지원하는 코인의 정보 및 현재가 가지고 오는 API
    fun getExchanges() = viewModelScope.launch {
        try {
            Log.d("ExchangeFragment", "ExchangeViewModel: 데이터 요청 시작")
            val result = exchangeRepository.getExchanges()
            if (!result.isNullOrEmpty()) {
                _exchanges.value = result
                Log.d("ExchangeFragment", "크립토 환율 정보 불러오기 성공: ${result.size}개의 데이터")
            } else {
                _exchanges.value = emptyList()
                Log.e("ExchangeFragment", "데이터가 비어있습니다.")
            }
        } catch (e: Exception) {
            Log.e("ExchangeFragment", "예기치 못한 오류 발생: ${e.message}")
            _exchanges.value = emptyList()
        }
    }

}