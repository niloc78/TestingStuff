package com.example.testingstuff

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class ErrandModel2TestA {

//    @Rule
//    @JvmField
//    val instantExexcutorRule = InstantTaskExecutorRule()
//
//    @Mock
//    lateinit var googleApiService : GoogleApiService
//
//    @Mock
//    var apiClient: ApiClient? = null
//
//    private var errandModel2: ErrandModel2? = null
//
//    @Mock
//    lateinit var observer : Observer<ArrayList<String>>
//
//    @Mock
//    lateinit var lifeCycleOwner : LifecycleOwner
//
//    lateinit var lifecycle : Lifecycle
//
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.initMocks(this)
//        lifecycle = LifecycleRegistry(lifeCycleOwner)
//        errandModel2 = ErrandModel2()
//        errandModel2!!.errands.observeForever(observer)
//
//    }
//    //fun addBestPlace(errand : String, query : String, location : String, r : Int, price_level : Int, rating : Double, chip_rating : Boolean, srcLatLng : DoubleArray) {
//    @Test
//    fun testNull() {
//        // `when`("")
//        val location = "42.3675294,-71.186966"
//        val latlng = doubleArrayOf(42.3675294, -71.186966)
//        errandModel2?.addBestPlace("buy pencils", "buy+pencils", location, 10000, 0, 1.0, false, latlng)
//        //assertTrue(errandModel2?.errands?.value!!.isNotEmpty())
//        assertTrue(errandModel2?.bestResults?.value!!.isNotEmpty())
//
//    }
//
//    @After
//    fun tearDown() {
//        apiClient = null
//        errandModel2 = null
//    }
}