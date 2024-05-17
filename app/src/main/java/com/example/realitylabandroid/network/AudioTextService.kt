package com.example.realitylabandroid.network

import com.example.realitylabandroid.api.AudioTextAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AudioTextService {
    public val retrofit:Retrofit
    public val apiService:AudioTextAPI
    init{
        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.11:5050")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(AudioTextAPI::class.java)
    }

    public fun sendText(text:Map<String,Any>){
        apiService.sendText(text).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                // handle the response
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // handle the failure
            }
        })
    }
}