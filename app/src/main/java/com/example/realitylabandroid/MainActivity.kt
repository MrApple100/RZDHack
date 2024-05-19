package com.example.realitylabandroid

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.realitylabandroid.databinding.ActivityMainBinding
import com.example.realitylabandroid.viewmodel.FileViewModel
import java.io.File


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

          //  viewModel.fetchFile(File(getPath(uri)))
        }
    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor: Cursor = getContentResolver().query(uri!!, projection, null, null, null)
        ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
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

