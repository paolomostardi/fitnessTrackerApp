package com.example.fitnesstrackerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class leaderBoard : AppCompatActivity() {


    //pass ad name and description
    private val usernames = arrayOf("Username1","Username 2","Username 3","Username 4")
    private val points = arrayOf(1000,200,3000,10)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        println("creating")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard_main)

        // setup recycler view
        println("making the view or something")
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view2)
        recyclerView.layoutManager = LinearLayoutManager(this)

        println("setting display")
        //set and display usernames
        recyclerView.adapter = CustomAdapter2(usernames,points)


        //leaderboard
        val leaderboardTitle = "Leaderboard position: "


        //if username == username in array from database
        // currentPosition == username index
        val currentPosition = 10
        val leaderboardView = findViewById<TextView>(R.id.leaderboardBanner)


        leaderboardView.text = leaderboardTitle+currentPosition


    }
}
