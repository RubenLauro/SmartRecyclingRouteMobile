package com.example.smartrecyclingroute

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_initial_report.*
import java.io.File
import java.io.IOException
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*


class IntialReportActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE: Int = 101

    val REQUEST_IMAGE_CAPTURE = 1

    private var mCurrentPhotoPath: String? = null

    val types_of_malfunctions = arrayOf("Damage","Breakdown","Full","Other")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_report)

        title = "Initial report"

        imageViewAddPhoto.setOnClickListener {
            if (checkPersmission())
                takePicture()
            else
                requestPermission()
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter(applicationContext, R.layout.dropdown_menu_popup_item, types_of_malfunctions)
        filled_exposed_dropdown.setAdapter(adapter)
        /*
        spinner_type!!.setOnItemSelectedListener(this)

        val array_adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types_of_malfunctions)
        array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_type!!.setAdapter(array_adapter)
         */

        btnScanQRCode.setOnClickListener {

            if (mCurrentPhotoPath != null && txt_field_description.text.toString().trim() != "" && txt_field_description.text.toString().isNotEmpty() && filled_exposed_dropdown.text.toString().isNotEmpty()) {
                val intent = Intent(this, ScanQRCodeActivity::class.java)
                intent.putExtra("photo_path", mCurrentPhotoPath)
                intent.putExtra("description", txt_field_description.text.toString())
                intent.putExtra("type", filled_exposed_dropdown.text.toString())
                startActivity(intent)
            }else{
                Snackbar.make(btnScanQRCode, R.string.error_empty_photo_description, Snackbar.LENGTH_SHORT).show()
            }
        }


    }

    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
            PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    takePicture()

                } else {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            //To get the File for further usage
            val auxFile = File(mCurrentPhotoPath)

            Picasso.get().load(auxFile).into(imageViewAddPhoto)
        }

        return super.onActivityResult(requestCode, resultCode, data)
    }

    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    private fun takePicture() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
            this,
            "com.example.android.fileprovider",
            file
        )

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }
}
