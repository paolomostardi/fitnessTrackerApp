package com.example.fitnesstrackerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class leaderBoard : AppCompatActivity() {


    //pass ad name and description




    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        println("creating")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard_main)
        val database = Database(this)
        val pair = database.Users().returnUsersByPoints()
        val usernames = pair.second.toTypedArray()
        var points = pair.first.toIntArray().toTypedArray()
        // setup recycler view
        println("making the view or something")
        val recyclerView2 = findViewById<RecyclerView>(R.id.recycler_view2)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        //set and display usernames
        recyclerView2.adapter = CustomAdapter2(usernames, points)
        //leaderboard
        val leaderboardTitle = "Leaderboard position: "
        //if username == username in array from database
        // currentPosition == username index
        val currentPosition = database.Users().getPositionByUsername("paolo")
        val leaderboardView = findViewById<TextView>(R.id.leaderboardBanner)
        leaderboardView.text = leaderboardTitle + currentPosition


    }
}
