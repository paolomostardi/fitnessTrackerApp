package com.example.fitnesstrackerapp

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchFriends : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_friends_main)
        val database = Database(this)
        val points= arrayOf("1","2","3")
        val button = findViewById<Button>(R.id.button2)
        val textToSearch = findViewById<TextView>(R.id.searchUserText)
        val layout = findViewById<LinearLayout>(R.id.layout)
        button.setOnClickListener(){
            val usernames = database.Users().usernameContainsString(textToSearch.text.toString())
            for (username in usernames) {
                var text = TextView(this)
                text.text = username
                layout.addView(text)
            }
            }





        }
    }