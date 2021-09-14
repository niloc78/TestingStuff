package com.example.testingstuff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonElement
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers


import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //val model : ErrandModel2 by viewModels()
    private var mDisposable: CompositeDisposable = CompositeDisposable()

    private val testAdapter = TestAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView : RecyclerView = findViewById(R.id.test_recycler_view)
        recyclerView.apply {
            adapter = testAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
       getStarredRepos("niloc78")
        getStarredRepos2("niloc78")
    }


    fun exampleRetroFitRxJava() {

    }

    override fun onDestroy() {

        mDisposable.dispose()

        super.onDestroy()
    }

    private fun getStarredRepos(userName : String) {

//        val observer = object : Observer<List<GitHubRepo>> {
//            override fun onSubscribe(d: Disposable) {
//                Log.d("observer Subscribed", "onSubscribe")
//            }
//
//            override fun onError(e: Throwable) {
//                Log.d("observable error", "onError")
//            }
//
//            override fun onComplete() {
//                Log.d("observable completed", "onComplete")
//            }
//
//            override fun onNext(t: List<GitHubRepo>) {
//                Log.d("observable next", "onNext")
//                testAdapter.apply {
//                    gitHubRepos = t
//                    notifyDataSetChanged()
//                }
//            }
//
//        }

        val disposableObserver = object : DisposableObserver<List<GitHubRepo>>() {
            override fun onNext(t: List<GitHubRepo>?) {
                if (t != null) {
                    testAdapter.apply {
                        gitHubRepos = t
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onError(e: Throwable?) {
                Log.d("observer Error", "onerror")
            }

            override fun onComplete() {
                Log.d("observer complete", "onComplete")
            }

        }

        val observable = GitHubClient.getInstance()
            .getStarredRepos(userName) // response observable <List<GitHubRepo>>, after this line do operators
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(disposableObserver)
//            .subscribe { t ->
//                Log.d("callback", "called")
//                Log.d("repoName", t.joinToString())
//                testAdapter.apply {
//                    gitHubRepos = t
//                    notifyDataSetChanged()
//                }
//
//            }

        mDisposable.add(observable)


    }

    private fun getStarredRepos2(userName : String) {
        val disposableObserver = object : DisposableObserver<JsonElement>() {


            override fun onError(e: Throwable?) {
                Log.d("observer Error", "onerror")
            }

            override fun onComplete() {
                Log.d("observer complete", "onComplete")
            }

            override fun onNext(t: JsonElement?) {

                Log.d("getStarredRepos2Response", "${t?.toString()}")
            }

        }
        val observable = GitHubClient.getInstance()
                .getStarredRepos2(userName) // response observable <List<GitHubRepo>>, after this line do operators
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableObserver)
        mDisposable.add(observable)
    }

    fun coldTest() {
        val observable = Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).timeInterval()
        Thread.sleep(5000)
        observable.subscribe{ i ->
            Log.d("coldTest", "From First: $i")
        }
    }
}