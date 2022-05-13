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

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.routine_main.*


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



    //STORE PAGE
    //pass multiple ads images
    private val img = arrayOf(R.drawable.ad,R.drawable.ad1,R.drawable.ad2,R.drawable.ad3)
    //pass ad name and description
    private val texts = arrayOf("Ad","Ad 1","Ad 2","Ad 3")
    private val desc = arrayOf("1000 points","2000 points","3000 points","4000 points")


    //LEADERBOARD PAGE
    //pass ad name and description
    private val usernames = arrayOf("Username1","Username 2","Username 3","Username 4")
    private val points = arrayOf(1000,200,3000,10)




    @SuppressLint("MissingPermission", "SetTextI18n")
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



        //STORE PAGE -CHANGE-
        // setup recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //set and display store items
        recyclerView.adapter = CustomAdapter(img,texts,desc)
        //set total points
        val currentPointTitle = "Total Points: "
        //change to variable that calculates points based on the distance
        val currentPoints = 2000
        val theTextView = findViewById<TextView>(R.id.totalPointsBanner)
        theTextView.text = currentPointTitle+currentPoints



        //LEADERBOARD PAGE
        // setup recycler view
        val recyclerView2 = findViewById<RecyclerView>(R.id.recycler_view2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //set and display usernames
        recyclerView2.adapter = CustomAdapter2(usernames,points)
        //leaderboard
        val leaderboardTitle = "Leaderboard position: "
        //if username == username in array from database
        // currentPosition == username index
        val currentPosition = 10
        val leaderboardView = findViewById<TextView>(R.id.leaderboardBanner)
        leaderboardView.text = leaderboardTitle+currentPosition


        //ROUTINE PAGE
        var todoList = mutableListOf(
            //dummy data - can delete
            Todo("PushUps", false),
            Todo("PullUps", false),
            Todo("SitUps", false),
            Todo("Crunches", false),
            Todo("BenchPress", false)
        )

        val adapter = TodoAdapter(todoList)
        rvTodos.adapter = adapter
        rvTodos.layoutManager = LinearLayoutManager(this)

        btnAddTodo.setOnClickListener{
            val title = etTodo.text.toString()
            val todo = Todo(title, false)
            todoList.add(todo)
            adapter.notifyItemInserted(todoList.size - 1)
        }

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