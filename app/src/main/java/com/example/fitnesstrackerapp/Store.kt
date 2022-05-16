package com.example.fitnesstrackerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Store: AppCompatActivity() {

    //pass multiple ads images
    private val img = arrayOf(R.drawable.ad,R.drawable.ad1,R.drawable.ad2,R.drawable.ad3)

    //pass ad name and description
    private val texts = arrayOf("Ad","Ad 1","Ad 2","Ad 3")
    private val desc = arrayOf("1000 points","2000 points","3000 points","4000 points")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_page)

        // setup recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set and display store items
        recyclerView.adapter = CustomAdapter(img,texts,desc)


        //set total points
        val currentPointTitle = "Total Points: "
        val currentPoints = 2000

        val theTextView = findViewById<TextView>(R.id.totalPointsBanner)

        theTextView.text = currentPointTitle+currentPoints


    }
}
