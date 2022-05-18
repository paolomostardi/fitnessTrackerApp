package com.example.fitnesstrackerapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.get

class SearchFriends : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_friends_main)
        val database = Database(this)

        val button = findViewById<Button>(R.id.button2)
        val textToSearch = findViewById<TextView>(R.id.searchUserText)
        button.setOnClickListener(){
                database.Users().usernameContainsString(textToSearch.text.toString())
            }





        }
    }