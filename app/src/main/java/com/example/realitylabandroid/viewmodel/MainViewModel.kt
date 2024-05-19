package com.example.realitylabandroid.viewmodel

import android.content.Context
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.realitylabandroid.MyRecognitionListener
import com.example.realitylabandroid.R
import com.example.realitylabandroid.repo.MainRepository
import com.example.realitylabandroid.utils.Attantioner
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.json.JSONObject
import org.vosk.LibVosk
import org.vosk.LogLevel
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.SpeechService
import org.vosk.android.SpeechStreamService
import org.vosk.android.StorageService
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


public class MainViewModel : ViewModel() {
   // lateinit var AUDIO_REQUEST:Int

    lateinit var mainRepository:MainRepository
    lateinit var context:Context
    lateinit var activity:AppCompatActivity

    lateinit var myRecognitionListener:MyRecognitionListener

    private var model: Model? = null
    private var speechService: SpeechService? = null
    private var speechStreamService: SpeechStreamService? = null

    lateinit var textButtonStopContinue:MutableLiveData<String>
    lateinit var enabledButtonStopContinue:MutableLiveData<Boolean>
    lateinit var toggleButtonStopContinue:MutableLiveData<Boolean>
    lateinit var linearLayout: LinearLayout
    lateinit var scrollView: ScrollView

    lateinit var startTime:MutableLiveData<Long>


    public fun create(activity:AppCompatActivity,context: Context,linearLayout: LinearLayout,scrollView: ScrollView) {
        this.context = context
        this.activity = activity
        this.linearLayout = linearLayout
        this.scrollView = scrollView


        mainRepository = MainRepository(context)
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

        textButtonStopContinue = MutableLiveData<String>(context.getString(R.string.pause))
        enabledButtonStopContinue = MutableLiveData<Boolean>(false);
        toggleButtonStopContinue = MutableLiveData<Boolean>(false);
        startTime = MutableLiveData<Long>(0)

//        inflateGreenCard(1000)
//        inflateGreenCard(6000)
//        inflateRedCard(10000)
        val sdf = SimpleDateFormat("HH:mm:ss")
        startTime.postValue(System.currentTimeMillis())
        val textTime = sdf.format(Date(System.currentTimeMillis()))
        inflateTextCard("Начало записи " + textTime,false)


    }

    public fun initModel() {
        StorageService.unpack(context, "model-en-us", "model",
            { model: Model? ->
                this.model = model
               // setUiState(State.READY)
                startMicroRecognise();
            },
            { exception: IOException ->
                setErrorState(
                    "Failed to unpack the model" + exception.message
                )
            })
    }



    var voiseText = MutableLiveData<String>()



    public fun startMicroRecognise(){
        if (speechService != null) {
           // setUiState(State.DONE)
            speechService!!.stop()
            speechService = null
        } else {
            Log.d("MICRO","first")

          //  setUiState(State.MIC)
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
    public fun inflateGreenCard(millisec:Long){
        val ltInflater: LayoutInflater = activity.layoutInflater
        val greenCard: View = ltInflater.inflate(R.layout.cardgreentextaudio, linearLayout, false)
        val hh = greenCard.findViewById<TextView>(R.id.hh);
        val mm = greenCard.findViewById<TextView>(R.id.mm);
        val ss = greenCard.findViewById<TextView>(R.id.ss);
        hh.text = "${millisec/3600000}"
        mm.text = "${millisec%3600000/60000}"
        ss.text = "${millisec%3600000%60000/1000}"
        linearLayout.addView(greenCard)
    }
    public fun inflateRedCard(millisec:Long){
        val ltInflater: LayoutInflater = activity.layoutInflater
        val redCard: View = ltInflater.inflate(R.layout.cardredtextaudio, linearLayout, false)
        val hh = redCard.findViewById<TextView>(R.id.hh);
        val mm = redCard.findViewById<TextView>(R.id.mm);
        val ss = redCard.findViewById<TextView>(R.id.ss);
        hh.text = "${millisec/3600000}"
        mm.text = "${millisec%3600000/60000}"
        ss.text = "${millisec%3600000%60000/1000}"
        linearLayout.addView(redCard)
    }

    public fun inflateTextCard(textstr:String,boolean: Boolean = true){
        val ltInflater: LayoutInflater = activity.layoutInflater
        val textCard: View = ltInflater.inflate(R.layout.cardtext, linearLayout, false)
        val text = textCard.findViewById<TextView>(R.id.cardTV)
        if(boolean) {
            val yellow_words = Attantioner.checkOnGoodWord(textstr)//yellow
            val red_words = Attantioner.checkOnWarningWord(textstr)//red
            val ss = Attantioner.drawRedYellow(textstr, red_words, yellow_words)

            text.text = ss
            linearLayout.addView(textCard)
            if (red_words.isNotEmpty()) {
                inflateRedCard(System.currentTimeMillis() - startTime.value!!)
            } else if (yellow_words.isNotEmpty()) {
                inflateGreenCard(System.currentTimeMillis() - startTime.value!!)
            }
        }else{
            text.text = textstr
            linearLayout.addView(textCard)
        }

        scrollView.post(Runnable {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)

        })

    }


    private fun setErrorState(message: String) {
        voiseText.postValue(message)
    }


    //for Listener

    fun onResult(hypothesis: String) {
        val jObject = JSONObject(hypothesis)
        val res = jObject.getString("text")
        println(res)
        voiseText.postValue(voiseText.value+
                (res + " " + FuzzySearch.ratio(
                    res,
                    "собака лежать"
                )).toString() + "\n"
        )
        if(!res.isEmpty()) {
            inflateTextCard(res)
            mainRepository.sendText(res)
        }

    }
    fun onFinalResult(hypothesis: String) {
        voiseText.postValue(voiseText.value + hypothesis + "\n")

        //setUiState(State.DONE)
        if (speechStreamService != null) {
            speechStreamService = null
        }
    }
    fun onPartialResult(hypothesis: String) {
        val jObject = JSONObject(hypothesis)
        val res = jObject.getString("partial")
      //  if(!res.isEmpty())
         //   voiseText.postValue(voiseText.value+res + "\n")
    }
    fun onError(eStr: String) {
        setErrorState(eStr)
    }
    fun onTimeout() {
      //  setUiState(State.DONE)
    }




}