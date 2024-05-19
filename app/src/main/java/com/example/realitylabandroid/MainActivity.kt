package com.example.realitylabandroid

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.realitylabandroid.databinding.ActivityMainBinding
import com.example.realitylabandroid.viewmodel.FileViewModel
import com.example.realitylabandroid.viewmodel.MainViewModel
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel:FileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(FileViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)

        viewModel.create(this,this, audioPickerActivity )
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this




        }


    val audioPickerActivity = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val file = uri.toFile()
            viewModel.fetchFile(file)
        }
    }

}




//    public val audioPickerActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
//        ActivityResultCallback { result ->
//            if (result.resultCode == RESULT_OK) {
//                val intent: Intent? = result.data
//                val file = contentResolver.openInputStream(intent?.data!!)
//                if (file != null) {
//                    val content = file.bufferedReader().readText()
//                    Toast
//                        .makeText(
//                            this,
//                            "File %s, Length %d bytes".format(intent.data!!.path, content.length),
//                            Toast.LENGTH_LONG
//                        )
//                        .show()
//                }
//            }
//        }
//    )

