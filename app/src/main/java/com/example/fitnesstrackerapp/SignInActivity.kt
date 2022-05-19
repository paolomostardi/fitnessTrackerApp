package com.example.fitnesstrackerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstrackerapp.databinding.ActivitySignInBinding
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var database = Database(this)

        if (MyApplication.loggedin)
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                //var loginDetails = true // change to DATABASE API check
                val loginValid = database.Users().existsByDetails(email, pass)
                println("DATABASE INFOS and stuff ---------------------------------------------")
                database.Users().addDummyData()
                val a = database.Users().usernameContainsString("paolo")
                if (loginValid) {
                    val intent = Intent(this, MainActivity::class.java)
                    MyApplication.username = email
                    MyApplication.loggedin = true
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Incorrect details", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onStart() {
        super.onStart()

        //get user from database
//        if(user != null){
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
        //DATABASE API
    }
}