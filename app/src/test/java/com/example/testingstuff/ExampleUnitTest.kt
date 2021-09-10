package com.example.testingstuff

import android.util.Log
import io.reactivex.Observable
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.rxkotlin.toObservable
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
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
        val observable = Observable.interval(1, TimeUnit.SECONDS)
        observable.subscribe{ i ->
            Log.d("coldTest", "From First: $i")

        }
    }
}