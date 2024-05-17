package com.example.realitylabandroid


import org.vosk.android.RecognitionListener


class MyRecognitionListener(
    val onResult:(String)->Unit,
    val onFinalResult:(String)->Unit,
    val onPartialResult:(String)->Unit,
    val  onError:(String) -> Unit,
    val onTimeout:()-> Unit
) : RecognitionListener {

    override fun onResult(hypothesis: String) {
        onResult.invoke(hypothesis)
    }

    override fun onFinalResult(hypothesis: String) {
        onFinalResult.invoke(hypothesis)
    }

    override fun onPartialResult(hypothesis: String) {
        onPartialResult.invoke(hypothesis)

    }

    override fun onError(e: Exception) {
        onError.invoke(e.message!!)
    }

    override fun onTimeout() {
        onTimeout.invoke()
    }
}