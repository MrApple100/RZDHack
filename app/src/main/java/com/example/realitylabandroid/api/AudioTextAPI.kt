package com.example.realitylabandroid.api

import retrofit2.Call
import retrofit2.http.POST

interface AudioTextAPI {
    @POST("/conversation/text")
    fun sendText(text:Map<String,Any>):Call<String>

}