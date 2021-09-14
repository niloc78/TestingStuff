package com.example.testingstuff

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Before
import androidx.activity.viewModels
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ErrandModel2Test {

    @Rule
    @JvmField
    val instantExexcutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var googleApiService : GoogleApiService

    @Mock
    var apiClient: ApiClient? = null

    @Mock
    var errandModel2 : ErrandModel2? = null

    @Mock
    lateinit var observer : Observer<ArrayList<String>>

    @Mock
    lateinit var lifeCycleOwner : LifecycleOwner

    @Mock
    lateinit var errandRepository : ErrandRepository

    lateinit var lifecycle : Lifecycle


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        lifecycle = LifecycleRegistry(lifeCycleOwner)
        errandModel2 = ErrandModel2()
        errandModel2!!.errands.observeForever { it ->
            System.out.println("observed!")
        }

    }
//fun addBestPlace(errand : String, query : String, location : String, r : Int, price_level : Int, rating : Double, chip_rating : Boolean, srcLatLng : DoubleArray) {
    @Test
    fun testNull() {
       // `when`("")
        val location = "42.3675294,-71.186966"
        val latlng = doubleArrayOf(42.3675294, -71.186966)
        errandModel2?.addBestPlace("buy pencils", "buy+pencils", location, 10000, 0, 1.0, false, latlng)
        //assertTrue(errandModel2?.errands?.value!!.isNotEmpty())
        assertTrue(errandModel2?.bestResults?.value!!.isNotEmpty())

    }
    //    fun getBestErrandResult(query : String, location : String, r : Int, price_level: Int, rating: Double, chip_rating: Boolean, srcLatLng : DoubleArray) : Observable<Result> {
    @Test
    fun testRepos() {
        val location = "42.3675294,-71.186966"
        val latlng = doubleArrayOf(42.3675294, -71.186966)
        val observable = ErrandRepository.getInstance().getBestErrandResult("buy+pencils", location, 20000, 1, 1.0, true, latlng)
            .doOnNext { t ->
                System.out.println("asdasf")
                val ex = t.name
                System.out.println(ex)
            }
            .subscribe()
        assertTrue(observable != null)
        Thread.sleep(5000)
    }

    //    fun getErrandResults(query : String, location : String, key : String) : Observable<ErrandResults2> {
    @Test
    fun testClient() {

        val location = "42.3675294,-71.186966"
        val latlng = doubleArrayOf(42.3675294, -71.186966)
        //val observable = apiClient?.getErrandResults("buy+pencils", location, BuildConfig.PLACES_API_KEY)
        val observable = ApiClient.getInstance().getErrandResults("buy+pencils", location, BuildConfig.PLACES_API_KEY)
            .doOnNext { t ->
                val s = t.next_page_token
                val exRe = t.results[0].name
                System.out.println(exRe)
            }
            .subscribe()
//        System.out.println("hi")
        assertTrue(observable != null)
        Thread.sleep(5000)
    }

    @Test
    fun testModel() {
        val location = "42.3675294,-71.186966"
        val latlng = doubleArrayOf(42.3675294, -71.186966)
        errandModel2?.addBestPlace("buy pencils","buy+pencils", location, 20000, 1, 1.0, false, latlng)
        Thread.sleep(5000)
        assertTrue(errandModel2?.errands?.value!!.isNotEmpty())
        assertTrue(errandModel2?.bestResults?.value!!.isNotEmpty())
        System.out.println(errandModel2?.bestResults?.value!!.joinToString())
        //errandModel2?.errands?.observeForever(observer)
        //verify(observer, times(1)).onChanged(errandModel2?.errands?.value!!)

    }

    @After
    fun tearDown() {
        apiClient = null
        errandModel2 = null
    }
}