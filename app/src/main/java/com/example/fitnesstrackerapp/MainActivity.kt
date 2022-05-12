package com.example.fitnesstrackerapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toolbar

import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices





class MainActivity : Activity(), LocationListener {
    protected var locationManager: LocationManager? = null
    protected var locationListener: LocationListener? = null
    protected var context: Context? = null
    var txtLat: TextView? = null
    var lat: String? = null
    var provider: String? = null
    protected var latitude: String? = null
    protected var longitude: String? = null
    protected var gps_enabled = false
    protected var network_enabled = false
    private lateinit var  toolbar: Toolbar

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        println("starting the activity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestLocationPermission()
        toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setActionBar(toolbar)
        txtLat = findViewById<View>(R.id.textview1) as TextView
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
    }

    override fun onLocationChanged(location: Location) {
        txtLat = findViewById<View>(R.id.textview1) as TextView
        val latitude =  location.latitude
        val longitude = location.longitude
        val speed = location.speed
        txtLat!!.text = "Latitude:" + latitude + ", Longitude:" + longitude
        println("CURRENT LOCATION CHANGED $latitude , $longitude, $speed ")
    }

    override fun onProviderDisabled(provider: String) {
        Log.d("Latitude", "disable")
    }

    override fun onProviderEnabled(provider: String) {
        Log.d("Latitude", "enable")
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Log.d("Latitude", "status")
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),0
        )
    }
}