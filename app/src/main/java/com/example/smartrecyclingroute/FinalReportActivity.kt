package com.example.smartrecyclingroute

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_final_report.*
import java.io.File

class FinalReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_report)

        val qr_content = intent.getStringExtra("qr_content")
        var splited_content = qr_content.split(";")
        txt_field_name.setText(splited_content[0])
        txt_field_latitude.setText(splited_content[1])
        txt_field_longitude.setText(splited_content[2])
        txt_field_description.setText(intent.getStringExtra("description"))
        val auxFile = File(intent.getStringExtra("photo_path"))
        Picasso.get().load(auxFile).into(imageViewPhoto)
    }
}
