package com.example.testingstuff

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonElement
import com.google.maps.android.SphericalUtil
import io.reactivex.rxjava3.core.Observable

class ErrandRepository {
    private val key = BuildConfig.PLACES_API_KEY

    companion object {
        val googleApiService : GoogleApiService = ApiClient.getInstance().googleApiService

        @Volatile
        private var instance : ErrandRepository? = null

//        private lateinit var context : Context
        private lateinit var prefs : SharedPreferences

        fun getInstance() : ErrandRepository { // insert context
            if (instance == null) {
                instance = ErrandRepository()
//                context = c
//                prefs = c.getSharedPreferences(c.packageName + "_preferences", Context.MODE_PRIVATE)
            }
            return instance!!
        }
    }

    fun getAllSavedSetsNames() : Observable<MutableMap.MutableEntry<String, out Any?>>? {
//        val prefs = this.getSharedPreferences(this.packageName + "_preferences", Context.MODE_PRIVATE)
//        val keys = prefs.all
//
//        if (!keys.isNullOrEmpty()) {
//            for (entry in keys.entries) {
//                //Log.d("map values", "${entry.key} : ${entry.value.toString()}")
//                if (entry.value is Set<*>) {
//                    setData.add(entry.key)
//                }
//            }
//            savedSetsAdapter.notifyDataSetChanged()
//        }
        val keys = prefs.all
        if (!keys.isNullOrEmpty()) {
            val entries = keys.entries
//            val setData = ArrayList<String>()
//            for (entry in entries) {
//
//            }
            return Observable.just(entries)
                .flatMapIterable { items -> items }
                .filter { item -> item.value is Set<*> }

        }
        return null
    }

    fun getBestErrandResult(query : String, location : String, r : Int, price_level: Int, rating: Double, chip_rating: Boolean, srcLatLng : DoubleArray) : Observable<Result> {
        return googleApiService.getErrandResults(query, location, key)
            .map { errResults ->
            //do filter functions here
            val results = errResults.results
            chooseBestPlace(results, calculateDistanceMatrix(results, srcLatLng), r, price_level, rating, chip_rating)
        }
    }

    fun getPolyResults(sourceId : String, waypoints : String) : Observable<JsonElement> {
        return googleApiService.getPolyResults(sourceId, waypoints, key)
    }

    //storeset

//    val prefs = c.getSharedPreferences(c.packageName + "_preferences", Context.MODE_PRIVATE)
//    if (prefs.getStringSet(setName, null).isNullOrEmpty()) {
//        if (errandModel.errands.value!!.isNotEmpty()) {
//            prefs.edit().putStringSet(setName, errandModel.errands.value!!.toHashSet()).commit()
//            (requireActivity() as ErrandActivity).apply {
//                setData.add(setName)
//                savedSetsAdapter.notifyItemInserted(setData.lastIndex)
//            }
//        } else {
//            Toast.makeText(c, "You must add at least one errand", Toast.LENGTH_SHORT).show()
//        }
//    } else {
//        Toast.makeText(c, "Set name already exists", Toast.LENGTH_SHORT).show()
//    }

    fun storeSet(setName : String, setValues : ArrayList<String>) {
        if (prefs.getStringSet("setName", null).isNullOrEmpty()) {
            if (setValues.isNotEmpty()) {
                prefs.edit().putStringSet(setName, setValues.toHashSet()).commit()
                //do something here to update recyclerview ui in sidebar
            }
        }
    }

    //load set

    //    val c = requireContext()
//    val prefs = c.getSharedPreferences(c.packageName + "_preferences", Context.MODE_PRIVATE)
//    val set = prefs.getStringSet(setName, null)
//    if (!set.isNullOrEmpty()) {
//        //Log.d("loadset", "set exists")
//        errandModel.errands.value = ArrayList(set.toTypedArray().toList())
//        locationChanged()
//    } else {
//        //Log.d("loadset", "set was null or empty")
//    }
    fun getSetValues(setName : String) : Observable<String>? {
        val trySet = prefs.getStringSet(setName, null)
        if (!trySet.isNullOrEmpty()) {
            return Observable.just(trySet.toTypedArray().toList())
                .flatMapIterable { setItems -> setItems }

        }

        return null
    }

    private fun chooseBestPlace(results : Array<Result>, distMatrix : IntArray, r : Int, price_level : Int, rating : Double, chip_rating : Boolean) : Result {
        var bestPlace = results[0]
        for (i in 0..results.size - 2) {
            if (distMatrix[i] > r) { // within radius
                continue
            }
            if (results[i].price_level != price_level && price_level != 0) { // price level selected and is the price target
                continue
            }
            if (results[i].rating < rating) { // rating is greater than or eq to min
                continue
            }
            bestPlace = if (chip_rating && results[i + 1].rating > bestPlace.rating) { // prioritize by rating
                results[i+1]
            } else {
                if ((bestPlace.price_level != price_level && price_level != 0)  || bestPlace.rating < rating) results[i] else bestPlace
            }
        }
        return bestPlace
    }

    private fun calculateDistanceMatrix(results : Array<Result>, srcLatLng : DoubleArray) : IntArray {
        val latLng = LatLng(srcLatLng[0], srcLatLng[1])
        val distMatrix = IntArray(results.size)
        for (i in 0..results.size-1) {
            val distance = SphericalUtil.computeDistanceBetween(latLng, LatLng(results[i].geometry["location"]!!["lat"]!! as Double, results[i].geometry["location"]!!["lng"]!! as Double))
            distMatrix[i] = distance.toInt()
        }
        return distMatrix
    }



//consider distMatrix, bestPlace, errand name

}