package com.example.fitnesstrackerapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.*

import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat


import com.google.maps.android.SphericalUtil
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.routine_main.*



import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(),LocationListener,NavigationView.OnNavigationItemSelectedListener {
    // variables for gps
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private var listOfLocations  = mutableListOf<Location>()
    private val locationPermissionCode = 2
    private var length = 0f.toDouble()


    private var updateLocation = false


    private lateinit var toolbar : Toolbar
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView : NavigationView



    override fun onCreate(savedInstanceState: Bundle?) {
        println("starting the activity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // location part
        val database = Database(this)
        val username = MyApplication.username
        val button: Button = findViewById(R.id.getLocation)
        val button2: Button = findViewById(R.id.getLocation2)
        val gainPointsButton = findViewById<Button>(R.id.convertToPoints)
        button.setOnClickListener {
            println("getting the location")
            tvGpsLocation = findViewById(R.id.textView)
            tvGpsLocation.text = "distance : " + length.toInt()
            updateLocation = true
            getLocation()
            println("finished with the location")

        }
        gainPointsButton.setOnClickListener{
            database.Users().addPoints(username,length.toInt())
            length = 0.0
            listOfLocations = mutableListOf<Location>()
        }
        button2.setOnClickListener {
            updateLocation = false

        }



        // menu part

        toolbar = findViewById(R.id.main_toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        setSupportActionBar(toolbar)
        val actionBarDrawerToggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)


    }

    private fun getLocation() {
        println("getting the location")
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            println("requesting permission")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        println("requesting location updates")
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    // ciaos
    // Xiao
    // ciao

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.title == "Home") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        if (item.title == "Store") {
            val intent = Intent(this, Store::class.java)
            startActivity(intent)
        }
        //todo create setting page
        if (item.title == "Settings") {
            val intent = Intent(this, Store::class.java)
            startActivity(intent)
        }

        if (item.title == "Routines") {
            val intent = Intent(this, RoutineTracker::class.java)
            startActivity(intent)
        }
        if (item.title == "LeaderBoard") {
            val intent = Intent(this, leaderBoard::class.java)
            startActivity(intent)
        }
        if (item.title == "UserSearch") {
            val intent = Intent(this, SearchFriends::class.java)
            startActivity(intent)
        }
        if (item.title == "Log out") {
            MyApplication.loggedin = false
            MyApplication.username = ""
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }


        println(item.title)




        return true
    }

    override fun onLocationChanged(location: Location) {
        println("finding the text view")
        tvGpsLocation = findViewById(R.id.textView)
        tvGpsLocation.text = "distance : " + length.toInt()
        listOfLocations.add(location)
        println(listOfLocations)
        updateLength()
        println("-----------------------------")
        println("the current length is $length")

    }

    private fun updateLength(){
        println("updating the length")
        val latLngs = mutableListOf<LatLng>()
        for (loc in listOfLocations) {
            latLngs.add(LatLng(loc.latitude, loc.longitude))
        }
        println("computing the length")
        length = SphericalUtil.computeLength(latLngs)
        length /= 10

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}




