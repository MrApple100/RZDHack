package com.example.realitylabandroid

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.realitylabandroid.databinding.ActivityMainBinding
import com.example.realitylabandroid.viewmodel.MainViewModel
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel:MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this


        if(allPermissionGranted()){
            viewModel.create(this)
            binding.executePendingBindings()
            binding.invalidateAll()
        }else{
            askPermissions()
        }


    }

    private final val PERMISSIONS = arrayOf(Manifest.permission.RECORD_AUDIO)

    public val requestLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), ActivityResultCallback { permissions ->
       val permissionGranted = AtomicBoolean(false)
       permissions.forEach { (key: String, value: Boolean) ->
           if (PERMISSIONS.contains(key) && !value
           ) {
               Toast.makeText(this, "$key $value", Toast.LENGTH_SHORT).show()
               permissionGranted.set(false)
           }
       }
       if (!permissionGranted.get()) {
           Toast.makeText(
               this,
               "Permission request denied",
               Toast.LENGTH_SHORT
           ).show()
       } else {
           viewModel.create(this)
       }


   }
   )

    private fun askPermissions() {
        requestLauncher.launch(PERMISSIONS)
    }

    private fun allPermissionGranted(): Boolean {
        val granted = AtomicBoolean(true)
        PERMISSIONS.forEach { it: String? ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    it!!
                ) == PackageManager.PERMISSION_DENIED
            ) {
                granted.set(false)
            }
        }
        Log.d("GRANTED", granted.get().toString())
        return granted.get()
    }
}