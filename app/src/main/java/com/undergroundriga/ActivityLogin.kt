package com.undergroundriga

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase


class ActivityLogin : AppCompatActivity() {

    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button
    lateinit var sharedPreferences: SharedPreferences

    var PREFS_KEY = "prefs"
    var USER_KEY = "user"
    var PWD_KEY = "pwd"

    var usr = ""
    var pwd = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        passwordEditText = findViewById(R.id.etPassword)
        usernameEditText = findViewById(R.id.etUsername)
        loginButton = findViewById(R.id.bSubmitLogin)
        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        usr = sharedPreferences.getString(USER_KEY, "").toString()
        pwd = sharedPreferences.getString(PWD_KEY, "").toString()

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (isValidCredentials(username, password)) {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(USER_KEY, username)
                editor.putString(PWD_KEY, password)
                editor.apply()

            } else {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidCredentials(username: String, password: String): Boolean {
        val db = DataBaseHandler(this)
        val data = db.readDataUsers()

        for (i in 0 until data.size) {
            val user = data[i]
            if (user.username == username && user.password == password) {
                val intent: Intent
                if (user.role == "1") {
                    intent = Intent(this, MainActivityAdmin::class.java)
                } else {
                    intent = Intent(this, MapsActivity::class.java)
                }
                startActivity(intent)
                return true
            }
        }

        return false
    }


    fun goToMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
