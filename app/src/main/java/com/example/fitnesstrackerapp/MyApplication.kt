package com.example.fitnesstrackerapp

import android.app.Application

class MyApplication : Application() {

    companion object {
        var username = ""
        var loggedin = false
    }
    override fun onCreate() {
        super.onCreate()
    }
}