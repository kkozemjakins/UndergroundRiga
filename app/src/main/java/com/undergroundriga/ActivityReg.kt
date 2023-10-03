package com.undergroundriga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivityReg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        btn_insert.setOnClickListner(
            if (etvUsername.text.toString().length > 0 &&
                etvPassword.text.toString().length > 0) ){

        }

    }
}
/*https://www.youtube.com/watch?v=OxHNcCXnxnE*/