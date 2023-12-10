package com.undergroundriga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class ActivityReg : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var regButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        passwordEditText = findViewById(R.id.etPassword)
        usernameEditText = findViewById(R.id.etUsername)
        regButton = findViewById(R.id.bSubmitReg)

        regButton.setOnClickListener {
            // Handle login button click here
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val role = "0"
            (
            if(username.length > 0 &&
                password.length > 0) {
                var user = User(username,password, role)
                var db = DataBaseHandler(this)
                db.insertData(user)

                /*var data = db.readDataUsers()
                var msg = ""
                for (i in 0..(data.size - 1)) {
                    msg = (data.get(i).id.toString() + " " + data.get(i).username + " " + data.get(i).password + " " + data.get(i).role + "\n")
                }*/
                Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()


            }else{
                Toast.makeText(this,"Please Fill All Data`s",Toast.LENGTH_SHORT).show()

            })


        }

    }
}