package com.example.smartrecyclingroute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanQRCodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null
    private lateinit var description: String
    private lateinit var currentPhotoPath: String

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view
        setContentView(mScannerView)                // Set the scanner view as the content view
        description = intent.getStringExtra("description")!!
        currentPhotoPath = intent.getStringExtra("photo_path")!!
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(rawResult: Result) {
        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        /**todo passar por intent a localização atual do user
         * todo criar duas locations uma com a distancia do user e a distancia lida do qrupo
         * todo verificar o distance between com o metodo distance between do Location
         * todo se for menor que x voltar para o mapa com o startActivity e mostrar um alerta
         */

        Log.v("tag", rawResult.text)
        val intent =  Intent(this, FinalReportActivity::class.java)
        intent.putExtra("qr_content", rawResult.text)
        intent.putExtra("description", description)
        intent.putExtra("photo_path", currentPhotoPath)
        startActivity(intent)
        finish()
        //onBackPressed()

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}
