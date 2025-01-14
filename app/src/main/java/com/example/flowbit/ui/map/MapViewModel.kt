package com.example.flowbit.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowbit.data.map.Place
import com.example.flowbit.data.map.Root
import com.example.flowbit.data.network.Exchange
import com.example.flowbit.data.repository.ExchangeRepository
import com.example.flowbit.data.repository.MapRepository
import kotlinx.coroutines.launch


class MapViewModel (val mapRepository: MapRepository) : ViewModel() {
    private var _places = MutableLiveData<List<Place>?>()
    val places = _places

    fun getPlaces(clientId: String,
                  clientSecret: String,
                  query: String,
                  display: Int,
                  start: Int,
                  sort: String) = viewModelScope.launch {
        try {
            val result = mapRepository.getPlaces(clientId, clientSecret, query, display, start, sort)
            if (result != null) {
                _places.value = result
            } else {
                _places.value = emptyList() // 빈 리스트 설정
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _places.value = emptyList()
        }
    }

}