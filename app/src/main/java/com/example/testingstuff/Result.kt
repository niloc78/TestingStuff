package com.example.testingstuff


data class Result(val business_status : String,
                  val price_level : Int,
                  val formatted_address : String,
                  val name : String,
                  val place_id : String,
                  val rating : Double,
                  val opening_hours : HashMap<String,Boolean>,
                  val geometry : HashMap<String, HashMap<String, Any>>
)