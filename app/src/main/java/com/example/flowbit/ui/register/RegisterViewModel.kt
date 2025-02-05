package com.example.flowbit.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowbit.data.entity.Expense
import com.example.flowbit.data.network.bsp.Exchange
import com.example.flowbit.data.network.ums.VerifyUserEmailRequest
import com.example.flowbit.data.network.ums.VerifyUserEmailResponse
import com.example.flowbit.data.repository.RegisterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel (val registerRepository: RegisterRepository): ViewModel() {

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


    private var _responseVerifyUserEmailResponse = MutableLiveData<VerifyUserEmailResponse>()
    val responseVerifyUserEmailResponse = _responseVerifyUserEmailResponse

    fun verifyUserEmailAddress(request: VerifyUserEmailRequest) = viewModelScope.launch {
        var result : VerifyUserEmailResponse
        withContext(Dispatchers.IO) {
            result = registerRepository.verifyUserEmailAddress(request)
        }
        _responseVerifyUserEmailResponse.value = result
    }
}