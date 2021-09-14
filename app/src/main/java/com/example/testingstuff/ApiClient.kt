package com.example.testingstuff

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    var googleApiService : GoogleApiService

    init {
        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create() // specify field naming properties to use
        val retrofit = Retrofit.Builder().baseUrl(API_BASE_URL).addCallAdapterFactory(
                RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        googleApiService = retrofit.create(GoogleApiService::class.java)
    }

    companion object {
        val API_BASE_URL = "https://maps.googleapis.com/maps/api/"

        @Volatile
        private var instance : ApiClient? = null

        fun getInstance() : ApiClient {
            if (instance == null) {
                instance = ApiClient()
            }
            return instance!!
        }
    }

    fun getErrandResults(query : String, location : String, key : String) : Observable<ErrandResults2> {
        return googleApiService.getErrandResults(query, location, key)
    }

    fun getPolyResults(sourceId : String, waypoints : String, key : String) : Observable<JsonElement> {
        return googleApiService.getPolyResults(sourceId, waypoints, key)
    }

}