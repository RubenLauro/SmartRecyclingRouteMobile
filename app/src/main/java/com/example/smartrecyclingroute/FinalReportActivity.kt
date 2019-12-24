package com.example.smartrecyclingroute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.smartrecyclingroute.Networking.RetrofitInitializer
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_final_report.*
import kotlinx.android.synthetic.main.activity_final_report.txt_field_description
import kotlinx.android.synthetic.main.activity_initial_report.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FinalReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_report)

        val qrContent = intent.getStringExtra("qr_content")
        val qtContentSplited = qrContent!!.split(";")
        txt_field_name.setText(qtContentSplited[0])
        txt_field_latitude.setText(qtContentSplited[1])
        txt_field_longitude.setText(qtContentSplited[2])
        val stringExtraDescription = intent.getStringExtra("description")
        txt_field_description.setText(stringExtraDescription)
        val auxFile = File(intent.getStringExtra("photo_path"))
        Picasso.get().load(auxFile).into(imageViewPhoto)
        val stringType = intent.getStringExtra("type")
        //FALTA AQUI O TXT_FIELD

        val compressedImageFile = Compressor(this).compressToFile(auxFile)

        btnReport.setOnClickListener {
            // Create a request body with file and image media type
            val fileReqBody = RequestBody.create(MediaType.parse("image/*"), compressedImageFile)
            // Create MultipartBody.Part using file request-body,file name and part name
            val part = MultipartBody.Part.createFormData("photo", compressedImageFile.name, fileReqBody)

            val groupName = RequestBody.create(MediaType.parse("text/plain"), qtContentSplited[0])
            val lat = RequestBody.create(MediaType.parse("text/plain"), qtContentSplited[1])
            val lon = RequestBody.create(MediaType.parse("text/plain"), qtContentSplited[2])
            val desctiption = RequestBody.create(MediaType.parse("text/plain"), stringExtraDescription!!)
            val type = RequestBody.create(MediaType.parse("text/plain"), stringType!!)

            //FALTA MANDAR NA CALL O TYPE PENSO
            val call = RetrofitInitializer().services().updateActivityTeamStatus(part, groupName, lat, lon, desctiption)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                   if(response.isSuccessful){
                       val intent = Intent(applicationContext, MapsActivity::class.java)
                       intent.putExtra("message", "✅️ Report created successfully")
                       startActivity(intent)
                       finish()
                   } else{
                       Snackbar.make(btnReport, R.string.response_error_text, Snackbar.LENGTH_SHORT).show()
                   }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("onFailure error", t?.message)
                }
            })
        }
    }
}
