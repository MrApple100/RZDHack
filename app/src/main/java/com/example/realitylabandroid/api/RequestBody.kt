package com.example.realitylabandroid.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class  RequestBody(
    @SerializedName("text")
    @Expose
    val text:String
)