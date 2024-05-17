package com.example.realitylabandroid.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.json.JSONObject
import org.vosk.LibVosk
import org.vosk.LogLevel
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.SpeechService
import org.vosk.android.SpeechStreamService
import org.vosk.android.StorageService
import com.example.realitylabandroid.MyRecognitionListener
import com.example.realitylabandroid.R
import com.example.realitylabandroid.viewmodel.component.State
import java.io.IOException
import java.io.InputStream


public class MainViewModel : ViewModel() {
    lateinit var context:Context
    lateinit var myRecognitionListener:MyRecognitionListener

    private var model: Model? = null
    private var speechService: SpeechService? = null
    private var speechStreamService: SpeechStreamService? = null

    lateinit var textButtonFile:MutableLiveData<String>
    lateinit var textButtonMicro:MutableLiveData<String>
    lateinit var textButtonStopContinue:MutableLiveData<String>
    lateinit var enabledButtonFile:MutableLiveData<Boolean>
    lateinit var enabledButtonMicro:MutableLiveData<Boolean>
    lateinit var enabledButtonStopContinue:MutableLiveData<Boolean>
    lateinit var toggleButtonStopContinue:MutableLiveData<Boolean>


    public fun create(context: Context) {
        this.context = context
        myRecognitionListener = MyRecognitionListener(
            ::onResult,
            ::onFinalResult,
            ::onPartialResult,
            ::onError,
            ::onTimeout);
        Log.d("ViewModel","first")
        LibVosk.setLogLevel(LogLevel.INFO)
        Log.d("ViewModel","second")

        initModel()
        Log.d("ViewModel","third")


        textButtonFile = MutableLiveData<String>(context.getString(R.string.recognize_file))
        textButtonMicro = MutableLiveData<String>(context.getString(R.string.recognize_microphone))
        textButtonStopContinue = MutableLiveData<String>(context.getString(R.string.pause))

        enabledButtonFile = MutableLiveData<Boolean>(false);
        enabledButtonMicro = MutableLiveData<Boolean>(false);
        enabledButtonStopContinue = MutableLiveData<Boolean>(false);
        toggleButtonStopContinue = MutableLiveData<Boolean>(false);

    }

    public fun initModel() {
        StorageService.unpack(context, "model-en-us", "model",
            { model: Model? ->
                this.model = model
                setUiState(State.READY)
            },
            { exception: IOException ->
                setErrorState(
                    "Failed to unpack the model" + exception.message
                )
            })
    }



    var voiseText = MutableLiveData<String>()

    public fun clickButtonFile(){
        setUiState(State.FILE)
        try {
            Thread(Runnable {
                val rec = Recognizer(
                    model, 90000f)//64000 //88200 //96000
                val ais: InputStream = context.getAssets().open(
                    "96k63.wav"
                )
               // if (ais.skip(44) != 44L) throw IOException("File too short")
                speechStreamService = SpeechStreamService(rec, ais, 90000f)
                speechStreamService!!.start(myRecognitionListener)
            }).start()

        } catch (e: IOException) {
            setErrorState(e.message!!)
        }
    }
    public fun clickButtonMicro() {
        if (speechService != null) {
            setUiState(State.DONE)
            speechService!!.stop()
            speechService = null
        } else {
            Log.d("MICRO","first")

            setUiState(State.MIC)
            Log.d("MICRO","Second")

            try {
                val rec = Recognizer(model, 16000.0f)
                Log.d("MICRO","third")

                speechService = SpeechService(rec, 16000.0f)
                Log.d("MICRO","fourth")

                speechService!!.startListening(myRecognitionListener)
                Log.d("MICRO","fifth")

            } catch (e: IOException) {
                setErrorState(e.message!!)
            }
        }
    }
    public fun clickPause() {
        if (speechService != null) {
            toggleButtonStopContinue.postValue(!toggleButtonStopContinue.value!!)
            speechService!!.setPause(!toggleButtonStopContinue.value!!)
            if(!toggleButtonStopContinue.value!!) {
                textButtonStopContinue.postValue(context.getString(R.string.continueRecognition))
            }else{
                textButtonStopContinue.postValue(context.getString(R.string.pause))
            }
        }
    }



    private fun setUiState(state: State) {

        when (state) {
            State.START -> {
                voiseText.postValue(context.getString(R.string.preparing))
               // resultView.setMovementMethod(ScrollingMovementMethod())
                enabledButtonFile.postValue( false)
                enabledButtonMicro.postValue( false)
                enabledButtonStopContinue.postValue(false)
            }

            State.READY -> {
                voiseText.postValue(context.getString(R.string.ready))
                enabledButtonFile.postValue(true)
                textButtonMicro.postValue(context.getString(R.string.recognize_microphone))
                enabledButtonMicro.postValue(true)
                enabledButtonStopContinue.postValue(false)
            }

            State.DONE -> {
                textButtonFile.postValue(context.getString(R.string.recognize_file))
                enabledButtonFile.postValue(true)
                textButtonMicro.postValue(context.getString(R.string.recognize_microphone))
                enabledButtonMicro.postValue(true)
                enabledButtonStopContinue.postValue(false)
            }

            State.FILE -> {
                voiseText.postValue(context.getString(R.string.starting))
                enabledButtonMicro.postValue(false)
                textButtonFile.postValue(context.getString(R.string.stop_file))
                enabledButtonFile.postValue(true)
                enabledButtonStopContinue.postValue(false)
            }

            State.MIC -> {
                voiseText.postValue(context.getString(R.string.say_something))
                enabledButtonFile.postValue( false)
                textButtonMicro.postValue(context.getString(R.string.stop_microphone))
                enabledButtonMicro.postValue(true)
                enabledButtonStopContinue.postValue(true)
            }

            else -> throw IllegalStateException("Unexpected value: $state")
        }
    }


    private fun setErrorState(message: String) {
        voiseText.postValue(message)
        textButtonMicro.value = context.getString(R.string.recognize_microphone)
        enabledButtonMicro.value = false
        enabledButtonFile.value = false
    }


    //for Listener

    fun onResult(hypothesis: String) {
        val jObject = JSONObject(hypothesis)
        val res = jObject.getString("text")
        println(res)
        voiseText.postValue(voiseText.value+
                (hypothesis + " " + FuzzySearch.ratio(
                    res,
                    "собака лежать"
                )).toString() + "\n"
        )

    }
    fun onFinalResult(hypothesis: String) {
        voiseText.postValue(voiseText.value + hypothesis + "\n")

        setUiState(State.DONE)
        if (speechStreamService != null) {
            speechStreamService = null
        }
    }
    fun onPartialResult(hypothesis: String) {
        val jObject = JSONObject(hypothesis)
        val res = jObject.getString("partial")
        //if(!res.isEmpty())
            voiseText.postValue(voiseText.value+res + "\n")
    }
    fun onError(eStr: String) {
        setErrorState(eStr)
    }
    fun onTimeout() {
        setUiState(State.DONE)
    }


}