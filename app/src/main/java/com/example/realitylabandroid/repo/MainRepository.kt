package com.example.realitylabandroid.repo

import com.example.realitylabandroid.network.AudioTextService

class MainRepository {
    val audioTextService = AudioTextService()
    init{

    }

    fun sendText(string: String){
        val body = mapOf(
            "text" to string
        )
        audioTextService.sendText(body)
    }

}