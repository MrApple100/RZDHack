package com.example.realitylabandroid.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.realitylabandroid.AudioRecognizeActivity
import com.example.realitylabandroid.R
import java.io.File

class FileViewModel:ViewModel() {
    lateinit var audioPickerActivity:ActivityResultLauncher<String>
    lateinit var context:Context
    lateinit var activity: AppCompatActivity




    lateinit var textButtonFile: MutableLiveData<String>
    lateinit var textButtonMicro: MutableLiveData<String>
    lateinit var enabledButtonFile: MutableLiveData<Boolean>
    lateinit var enabledButtonMicro: MutableLiveData<Boolean>

    fun create(context: Context,activity: AppCompatActivity,audioPickerActivity: ActivityResultLauncher<String>){
        this.context = context
        this.activity = activity
        this.audioPickerActivity = audioPickerActivity

        textButtonFile = MutableLiveData<String>(context.getString(R.string.recognize_file))
        textButtonMicro = MutableLiveData<String>(context.getString(R.string.recognize_microphone))

        enabledButtonFile = MutableLiveData<Boolean>(false);
        enabledButtonMicro = MutableLiveData<Boolean>(false);
    }

    public fun clickButtonMicro() {
        val intent = Intent(context, AudioRecognizeActivity::class.java)
        ActivityCompat.startActivityForResult(activity, intent, 0, null)
    }

    public fun clickButtonFile(){
        //setUiState(State.FILE)
//        try {
//            Thread(Runnable {
//                val rec = Recognizer(
//                    model, 90000f)//64000 //88200 //96000
//                val ais: InputStream = context.getAssets().open(
//                    "96k63.wav"
//                )
//               // if (ais.skip(44) != 44L) throw IOException("File too short")
//                speechStreamService = SpeechStreamService(rec, ais, 90000f)
//                speechStreamService!!.start(myRecognitionListener)
//            }).start()
//
//        } catch (e: IOException) {
//            setErrorState(e.message!!)
//        }
        openGalleryForAudio()
        //mainRepository.uploadFile()
    }

    fun openGalleryForAudio() {
        val choose = "audio/*"



        audioPickerActivity.launch(choose)
//        val videoIntent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
//       // startActivityForResult(activity,Intent.createChooser(videoIntent, "Select Audio"), AUDIO_REQUEST,null)
//        audioPickerActivity.launch(Intent.createChooser(videoIntent, "Select Audio"))
    }

    fun fetchFile(file: File) {
        file.name
    }

}