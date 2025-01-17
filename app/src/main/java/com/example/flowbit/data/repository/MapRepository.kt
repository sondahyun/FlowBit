package com.example.flowbit.data.repository
import android.util.Log
import com.example.flowbit.data.map.MapService
import com.example.flowbit.data.map.Place
import com.example.flowbit.data.map.Root


class MapRepository (private val mapService: MapService) {

    suspend fun getPlaces(
        clientId: String,
        clientSecret: String,
        query: String,
        display: Int,
        start: Int,
        sort: String
    ) : List<Place>?
    {
        try {
            val response = mapService.getPlaces(clientId, clientSecret, query, display, start, sort)
            Log.d("mapRepository", "Response: $response")
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
