package com.example.testingstuff

import androidx.annotation.NonNull
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement

import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject


import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

import retrofit2.converter.gson.GsonConverterFactory


class GitHubClient {
    var gitHubService: GitHubService

    init {
        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create() // specify field naming properties to use
        val retrofit = Retrofit.Builder().baseUrl(GITHUB_BASE_URL).addCallAdapterFactory(
            RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        gitHubService = retrofit.create(GitHubService::class.java)
    }

    companion object {
        val GITHUB_BASE_URL = "https://api.github.com/"

        @Volatile
        private var instance : GitHubClient? = null

        fun getInstance() : GitHubClient {
            if (instance == null) {
                instance = GitHubClient()
            }
            return instance!!
        }

    }

    fun getStarredRepos(@NonNull userName : String) : Observable<List<GitHubRepo>> {
        return gitHubService.getStarredRepositories(userName)
    }
    fun getStarredRepos2(@NonNull userName : String) : Observable<JsonElement> {
        return gitHubService.getStarredRepositories2(userName)
    }

}