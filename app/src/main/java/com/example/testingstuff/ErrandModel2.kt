package com.example.testingstuff

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.PolyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ErrandModel2 @Inject constructor() : ViewModel() {

    private var mDisposableContainer : CompositeDisposable = CompositeDisposable()

    val errands = MutableLiveData<ArrayList<String>>(ArrayList()) //observe from errandFrag
    val currPlaceInfo = MutableLiveData<HashMap<String, Any>>() // latLng and id, observe this from ErrandFragment and mapFrag to update curr marker

    val markers = MutableLiveData<ArrayList<Marker>>(ArrayList())

    val bestResults = MutableLiveData<ArrayList<Result>>(ArrayList()) //observe this from errandFrag and also mapFrag

//    private val context = getApplication<Application>().applicationContext

    val setData = MutableLiveData<ArrayList<String>>(ArrayList())
    val errandData = MutableLiveData<ArrayList<LinkedHashMap<String, String>>>()

    val pathDecodedPoly = MutableLiveData<List<LatLng>>() // observe this from mapfragment

    override fun onCleared() {
        if (!mDisposableContainer.isDisposed) {
            mDisposableContainer.dispose()
        }
        super.onCleared()
    }

    fun storeCurrentSet(setName : String) {
        if (errands.value != null) {
            ErrandRepository.getInstance().storeSet(setName, errands.value!!) // insert context
        }
    }

    fun getAndLoadSet(setName: String, r : Int, price_level: Int, rating: Double, chip_rating: Boolean) {
        val lat = (currPlaceInfo.value!!["latLng"] as DoubleArray)[0]
        val lng = (currPlaceInfo.value!!["latLng"] as DoubleArray)[1]
        val observable = ErrandRepository.getInstance().getSetValues(setName) // insert context
                ?.doOnNext { errand ->
                    addBestPlace(errand, errand, "$lat,$lng", r, price_level, rating, chip_rating, currPlaceInfo.value!!["latLng"] as DoubleArray)
                }
                ?.doOnError { e -> Log.d("getSet error", "error") }
                ?.doOnComplete {
                    Log.d("getSet complete", "completed")
                }
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe()
        mDisposableContainer.add(observable)

    }

    fun getAllSavedSetsNames() {
        val observable = ErrandRepository.getInstance().getAllSavedSetsNames() // insert context
                ?.doOnNext { entry ->
                    setData.value!!.add(entry.key)
                    setData.notifyObserver()
                }
                ?.doOnError { e -> Log.d("getAllSavedSetsNames error", "error") }
                ?.doOnComplete { Log.d("getAllSavedSetsNames complete", "completed") }
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe()
        mDisposableContainer.add(observable)
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.postValue(this.value)
    }

    fun addBestPlace(errand : String, query : String, location : String, r : Int, price_level : Int, rating : Double, chip_rating : Boolean, srcLatLng : DoubleArray) {
        errands.value?.add(errand)
        errands.notifyObserver()
//        R.layout.errand_item_layout -> (holder as ErrandViewHolder).bind(data[position]["errandName"]!!, data[position]["storeName"]!!,
//        data[position]["address"]!!)
        val observable = ErrandRepository.getInstance().getBestErrandResult(query, location, r, price_level, rating, chip_rating, srcLatLng) // insert context
                .filter { t -> t != null }
                .doOnNext { t ->
                    bestResults.value?.add(t)
                    bestResults.notifyObserver()
                    System.out.println("hi")
                    errandData.value?.add(linkedMapOf("errandName" to errand, "storeName" to t.name, "address" to t.formatted_address))
                    errandData.notifyObserver()
//                    Log.d("newBestPlaceAdded: ", "${bestResults.value?.last()?.name}")
//                    Log.d("resultingBestResults: ", "${bestResults.value?.joinToString()}")
                }
                .doOnError { e ->
                    //bestResults.value?.add(Result("", 1, "", "", "", 1.2, hashMapOf(), hashMapOf()))
//                    Log.d("disposableObserver error", "error ${e?.printStackTrace()}")
                }
                .doOnComplete {
//                    Log.d("disposableObserver complete", "completed")
                }
                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        mDisposableContainer.add(observable)

    }
// path
//    val routes = response.getJSONArray("routes")
//    val route = routes.getJSONObject(0)
//    val polyline = route.getJSONObject("overview_polyline")
//    val overviewPoly = polyline.getString("points")
//    //Log.d("overview_polyline", "overview polyline: $polyline")
//    val decodedPoly = PolyUtil.decode(overviewPoly)
//    //Log.d("decoded_poly", "decoded poly: $decodedPoly")
//    onSuccess(decodedPoly)

    fun updatePoly(sourceId : String, waypoints : String) {

        val observable = ErrandRepository.getInstance().getPolyResults(sourceId, waypoints) // insert context
                .doOnNext { t ->
                    val overviewPoly = t.asJsonObject["routes"]
                            .asJsonArray[0]
                            .asJsonObject["overview_polyline"]
                            .asJsonObject["points"]
                            .asString
                    pathDecodedPoly.value = PolyUtil.decode(overviewPoly)
                    pathDecodedPoly.notifyObserver()
                    Log.d("polyLine updated", "updated")
                }
                .doOnError { e -> Log.d("polyObserver error", "error ${e?.printStackTrace()}")}
                .doOnComplete { Log.d("polyObserver complete", "completed") }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        mDisposableContainer.add(observable)

    }








}