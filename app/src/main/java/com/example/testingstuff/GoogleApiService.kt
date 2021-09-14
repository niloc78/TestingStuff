package com.example.testingstuff

import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleApiService {
// places
//    val currErrand = errands.value!!.last()
//    val lat = (currPlaceInfo.value!!["latLng"] as DoubleArray)[0]
//    val lng = (currPlaceInfo.value!!["latLng"] as DoubleArray)[1]
//    val location = "&location=$lat,$lng"
//    val query = "&query=" + currErrand.replace(" ", "+")
//    val key = "&key=$key"
//    val rankby = "&rankby=distance"
//    val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?$query$location$rankby$key"


    //directions
//    val mGetUrlContent = GetUrlContent(mIResult, context)
//    val origin = "origin=place_id:$sourceId"
//    val destination = "&destination=place_id:$sourceId"
//    var waypoints = "&waypoints=optimize:true|" + "place_id:" + bestResults[0].place_id
//    val api_key = "&key=" + BuildConfig.PLACES_API_KEY
//    for (i in 1 until bestResults.size) {
//        waypoints += "|place_id:" + bestResults[i].place_id
//    }
//    val url =
//        "https://maps.googleapis.com/maps/api/directions/json?$origin$destination$waypoints$api_key"
//    mGetUrlContent.getDataVolley("GETCALL", url)




    @GET("place/textsearch/json?")
    fun getErrandResults(@Query("query") query : String, @Query("location") location : String, @Query("key") key : String) : Observable<ErrandResults2>

    @GET("directions/json?origin=place_id:{sourceId}&destination=place_id:{sourceId}&waypoints=optimize:true|place_id:{waypoints}&key={key}")
    fun getPolyResults(@Path("sourceId") sourceId : String, @Path("waypoints") waypoints : String, @Path("key") key : String) : Observable<JsonElement>

}