package com.example.realitylabandroid.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AudioTextAPI {
    @POST("/conversation/text")
    fun sendText(@Body text:com.example.realitylabandroid.api.RequestBody):Call<com.example.realitylabandroid.api.RequestBody>
    @Multipart
    @POST("/conversation/file")
    fun uploadAudioFile(@Part filePart: MultipartBody.Part):Call<String>
}