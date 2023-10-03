package com.undergroundriga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class ActivityLogin : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.etPassword)
        passwordEditText = findViewById(R.id.etUsername)
        loginButton = findViewById(R.id.bSubmitLogin)

        loginButton.setOnClickListener {
            // Handle login button click here
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (isValidCredentials(username, password)) {
                // Successful login, you can navigate to another activity or perform other actions
                Toast.makeText(this, "Username:"+username+" password:"+password, Toast.LENGTH_SHORT).show()
            } else {
                // Invalid credentials, show an error message
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidCredentials(username: String, password: String): Boolean {
        if (username=="admin" && password=="admin") {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            return true
        } else {
            return false
        }
    }

    fun goToMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    data class User(val id: Int, val username: String, val password: String)






}