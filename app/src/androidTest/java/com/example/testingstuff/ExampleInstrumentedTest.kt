package com.example.testingstuff

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.rxkotlin.toObservable

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.testingstuff", appContext.packageName)
    }
    @Test
    fun test1() {
        val observable = listOf(1,2,3).toObservable()
    }

    @Test
    fun observableTest() {
        val observable = listOf(1, 1, 2, 3).toObservable()
//        observable.test().assertValues(1,1,2,3)
        observable.buffer(2).test().assertValues(listOf(1,1), listOf(2,3))

    }
    @Test
    fun flowableTest() {
        val flowable = listOf(1,1,2,3).toFlowable()
        flowable.buffer(2).test().assertValues(listOf(1,1,2,3))
    }
    @Test
    fun coldTest() {
        val observable = Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).timeInterval()
        Thread.sleep(5000)
        observable.subscribe{ i ->
            Log.d("coldTest", "From First: $i")
        }
        Thread.sleep(5000)
    }
    @Test
    fun hotTest() {
        val observable = Observable.interval(1, TimeUnit.SECONDS).publish()
        observable.connect()
        Thread.sleep(5000)
        val subscription = observable.subscribe { i ->
            Log.d("hotTest", "From First: $i")
        }
    }

    @Test
    fun subscribeTest() {
        val observable = Observable.just(1,2,3)
        val observer = object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                Log.d("observer onsubscribe", "subscribed")
            }

            override fun onNext(t: Int) {
                Log.d("emitted onNext int", "$t")
            }

            override fun onError(e: Throwable) {
                Log.d("observable error", "error produced")
            }

            override fun onComplete() {
                Log.d("observable complete", "completed")
            }

        }
        observable.subscribe(observer)
//        val sub = object: Subscriber<Int>{
//            override fun onSubscribe(s: Subscription?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onNext(t: Int?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onError(t: Throwable?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onComplete() {
//                TODO("Not yet implemented")
//            }
//
//        }

//        observable.subscribe { i ->
//            Log.d("int", "$i")
//        }
    }


}