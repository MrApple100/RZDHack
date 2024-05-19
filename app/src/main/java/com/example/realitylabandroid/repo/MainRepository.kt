package com.example.realitylabandroid.repo

import android.content.Context
import com.example.realitylabandroid.network.AudioTextService

import java.io.File

class MainRepository(val context: Context) {
    val audioTextService = AudioTextService(context)
    init{

    }

    fun sendText(string: String){
        val requestBody = com.example.realitylabandroid.api.RequestBody(string)
        audioTextService.sendText(requestBody)
    }

    fun uploadFile(file:File){
        audioTextService.uploadFile(file)
    }

}