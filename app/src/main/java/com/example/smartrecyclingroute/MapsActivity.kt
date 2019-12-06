package com.example.smartrecyclingroute

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.smartrecyclingroute.Model.GroupList
import com.example.smartrecyclingroute.Networking.RetrofitInitializer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var lastLocation: Location

    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var map: GoogleMap

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        var locationExtra = intent.extras?.get("location")
        if (locationExtra != null){
            lastLocation = locationExtra as Location
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        } else{
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            // 2
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                // Got last known location. In some rare situations this can be null.
                // 3
                if (location != null) {
                    lastLocation = location
                    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this)
                }
            }
        }

        val stringMessage = intent.getStringExtra("message")
        if (stringMessage != null){
            Snackbar.make(contraint_layout_map, stringMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun setUpMap() {
        // 1
        map.isMyLocationEnabled = true

        val currentLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

        val call = RetrofitInitializer().services().listGroups()
        call.enqueue(object : Callback<GroupList> {
            override fun onResponse(call: Call<GroupList>, response: Response<GroupList>) {
                response.body()?.data?.forEach {
                        val groupLocation = LatLng(it.lat, it.lon)
                        map.addMarker(MarkerOptions().position(groupLocation).title(it.name).snippet("Show details")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.info)))
                                /*.fromResource(R.drawable.info)*/
                    }
            }

            override fun onFailure(call: Call<GroupList>, t: Throwable) {
                Log.e("onFailure error", t?.message)
            }
        })

        map.setOnInfoWindowClickListener {
            val intent =  Intent(this, EcopontosActivity::class.java)
            intent.putExtra("group_name", it.title)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.camera_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent =  Intent(this, IntialReportActivity::class.java)
        startActivity(intent)
        return true
    }
}
