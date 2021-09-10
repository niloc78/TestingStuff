package com.example.testingstuff



import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubService {
    @GET("users/{user}/starred")
    fun getStarredRepositories(@Path("user") userName : String) : Observable<List<GitHubRepo>>

    @GET("users/{user}/starred")
    fun getStarredRepositories2(@Path("user") userName : String) : Observable<JsonElement>

}