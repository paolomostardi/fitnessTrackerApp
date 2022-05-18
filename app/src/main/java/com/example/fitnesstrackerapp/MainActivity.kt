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
    private val listOfLocations  = mutableListOf<Location>()
    private val locationPermissionCode = 2
    private var length = 0f.toDouble()


    private var updateLocation = false


    private lateinit var toolbar : Toolbar
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView : NavigationView



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




    override fun onCreate(savedInstanceState: Bundle?) {
        println("starting the activity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // location part
        val button: Button = findViewById(R.id.getLocation)
        val button2: Button = findViewById(R.id.getLocation2)
        button.setOnClickListener {
            println("getting the location")
            tvGpsLocation = findViewById(R.id.textView)
            tvGpsLocation.text = "distance : " + length.toInt()
            updateLocation = true
            getLocation()
            println("finished with the location")

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
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
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
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }


        println(item.title)




        return true
    }

    override fun onLocationChanged(location: Location) {
        tvGpsLocation = findViewById(R.id.textView)
        tvGpsLocation.text = "distance : " + length.toInt()
        listOfLocations.add(location)
        println(listOfLocations)
        updateLength()
        println("-----------------------------")
        println("the current length is $length")

    }

    private fun updateLength(){
        val latLngs = mutableListOf<LatLng>()
        for (loc in listOfLocations) {
            latLngs.add(LatLng(loc.latitude, loc.longitude))
        }
        length = SphericalUtil.computeLength(latLngs)
        length /= 50

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

    fun renderPages(){

        //STORE PAGE -CHANGE-
        // setup recycler view
        println("creating store page")
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        print("testing ")
        recyclerView.layoutManager = LinearLayoutManager(this)
        println("setting display and store items")
        //set and display store items
        recyclerView.adapter = CustomAdapter(img, texts, desc)
        //set total points
        val currentPointTitle = "Total Points: "
        //change to variable that calculates points based on the distance
        val currentPoints = 2000
        val theTextView = findViewById<TextView>(R.id.totalPointsBanner)
        theTextView.text = currentPointTitle + currentPoints


        //LEADERBOARD PAGE
        println("creating leaderboard page")
        // setup recycler view



        //ROUTINE PAGE
        println("creating routine page")
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

        btnAddTodo.setOnClickListener {
            val title = etTodo.text.toString()
            val todo = Todo(title, false)
            todoList.add(todo)
            adapter.notifyItemInserted(todoList.size - 1)
        }

    }

}




