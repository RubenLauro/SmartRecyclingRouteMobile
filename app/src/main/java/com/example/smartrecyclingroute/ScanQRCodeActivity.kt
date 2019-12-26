package com.example.smartrecyclingroute

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_final_report.*
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.android.synthetic.main.activity_splash_screen.*
import me.dm7.barcodescanner.zxing.ZXingScannerView


class ScanQRCodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null
    private lateinit var description: String
    private lateinit var currentPhotoPath: String
    private lateinit var currentType: String

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var lastLocation: Location

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        title = "Scan Qr code"
        //todo talvez adicionar menu item com ajuda na localização do qr code
        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view
        setContentView(mScannerView)                // Set the scanner view as the content view
        description = intent.getStringExtra("description")!!
        currentPhotoPath = intent.getStringExtra("photo_path")!!
        currentType = intent.getStringExtra("type")!!
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

        val qtContentSplited = rawResult.text.split(";")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 2

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location

                val qrLoc = Location("").apply {
                    latitude = qtContentSplited[1].toDouble()
                    longitude = qtContentSplited[2].toDouble()
                }


                val distance: Float = lastLocation.distanceTo(qrLoc)
                print(distance)

                if (distance > 25){
                    Toast.makeText(this, R.string.error_far_from_ecoponto, Toast.LENGTH_LONG).show()
                    mScannerView!!.stopCamera()
                    mScannerView!!.setResultHandler(this)
                    mScannerView!!.startCamera()
                } else{
                    val intent =  Intent(this, FinalReportActivity::class.java)
                    intent.putExtra("qr_content", rawResult.text)
                    intent.putExtra("description", description)
                    intent.putExtra("photo_path", currentPhotoPath)
                    intent.putExtra("type", currentType)
                    startActivity(intent)
                   // finish()
                }
            }
        }

            /*
        Log.v("tag", rawResult.text)
        val intent =  Intent(this, FinalReportActivity::class.java)
        intent.putExtra("qr_content", rawResult.text)
        intent.putExtra("description", description)
        intent.putExtra("photo_path", currentPhotoPath)
        startActivity(intent)
        */

        //onBackPressed()

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}
