package com.example.smartrecyclingroute

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.smartrecyclingroute.Model.GroupList
import com.example.smartrecyclingroute.Networking.RetrofitInitializer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var map: GoogleMap

    private lateinit var location: Location

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        location = intent.extras?.get("location") as Location
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        // 1
        map.isMyLocationEnabled = true


        // Got last known location. In some rare situations this can be null.
        // 3

        val currentLatLng = LatLng(location.latitude, location.longitude)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

        val call = RetrofitInitializer().services().listEcopontos()
        call.enqueue(object : Callback<GroupList> {
            override fun onResponse(call: Call<GroupList>, response: Response<GroupList>) {
                response.body()?.data?.forEach {
                        val group_location = LatLng(it.lat, it.lon)
                        map.addMarker(MarkerOptions().position(group_location).title(it.name))
                    }
            }

            override fun onFailure(call: Call<GroupList>, t: Throwable) {
                Log.e("onFailure error", t?.message)
            }
        })
    }
}
