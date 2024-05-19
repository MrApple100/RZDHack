package com.example.realitylabandroid.network

import android.R.attr.data
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import com.example.realitylabandroid.api.AudioTextAPI
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream


class AudioTextService(val context: Context) {
    public val retrofit:Retrofit
    public val apiService:AudioTextAPI
    val handler:Handler
    init{
        val okHttpClient = OkHttpClient.Builder()
            .build()
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://51.250.19.25:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(AudioTextAPI::class.java)
        handler = Handler(Looper.getMainLooper()){ it->
            Toast.makeText(context,"Запись успешно отправлена!",Toast.LENGTH_LONG).show()
            true
        }
    }

    public fun sendText(text:com.example.realitylabandroid.api.RequestBody){
        apiService.sendText(text).enqueue(object : Callback<com.example.realitylabandroid.api.RequestBody> {
            override fun onResponse(call: Call<com.example.realitylabandroid.api.RequestBody>, response: Response<com.example.realitylabandroid.api.RequestBody>) {
               // Log.d("RESPONSE","${response.body()!!.text}")
               // Log.d("RESPONSE","${response.code()}")
              //  Log.d("RESPONSE","${response.code()}")

                val msg = Message()
                handler.sendMessage(msg)



            }

            override fun onFailure(call: Call<com.example.realitylabandroid.api.RequestBody>, t: Throwable) {
                Log.d("FAILFAIL",t.message.toString())
              //  Log.d("FAILFAIL","${call.request().body().}")


            }
        })
    }
    public fun uploadFile(audioFile:File){
       // val filePart = packFile(context, "audio[file]", Uri.fromFile(audioFile))!!
//        apiService.uploadAudioFile(filePart).enqueue(object : Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                // handle the response
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                // handle the failure
//            }
//        })
    }

//    @Nullable
//    fun packFile(context: Context, partName: String, @Nullable fileUri: Uri?): MultipartBody.Part? {
//        if (fileUri == null) return null
//        val cr = context.contentResolver
//        var tp = cr.getType(fileUri)
//        if (tp == null) {
//            tp = "audio"
//        }
//        return try {
//            val iStream = context.contentResolver.openInputStream(fileUri)
//            val inputData = getBytes(iStream)
//            val requestFile = RequestBody.create(MediaType.parse(tp), inputData)
//            MultipartBody.Part.createFormData(partName, fileUri.lastPathSegment, requestFile)
//        } catch (e: Exception) {
//            null
//        }
//    }

    @Nullable

    private fun getBytes(@Nullable inputStream: InputStream?): ByteArray? {
        if (inputStream == null) return null
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}