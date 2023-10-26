package com.undergroundriga

import android.content.ClipDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class MapsActivityAdmin: AppCompatActivity() {

    private lateinit var btn_insert: Button
    private lateinit var btn_delete: Button
    private lateinit var btn_update: Button
    private lateinit var btn_read: Button
    private lateinit var etPlaceId: EditText

    private lateinit var etPlaceName: EditText
    private lateinit var etDescription : EditText
    private lateinit var etTag: EditText
    private lateinit var etPosX: EditText
    private lateinit var etPosY: EditText

    private lateinit var tvResult: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_admin)

        val context = this
        var db = DataBaseHandler(context)

        // Initialize your views by finding them with their IDs
        btn_insert = findViewById(R.id.btn_insert)
        btn_delete = findViewById(R.id.btn_delete)
        btn_update = findViewById(R.id.btn_update)
        btn_read = findViewById(R.id.btn_read)
        etPlaceId = findViewById(R.id.etPlaceId)

        etPlaceName = findViewById(R.id.etPlaceName)
        etDescription = findViewById(R.id.etDescription)
        etTag = findViewById(R.id.etTag)
        etPosX = findViewById(R.id.etPosX)
        etPosY = findViewById(R.id.etPosY)

        tvResult = findViewById(R.id.tvResult)

        btn_insert.setOnClickListener({
            if (etPlaceName.text.toString().length > 0 && etDescription.text.toString().length > 0 && etTag.text.toString().length > 0 && etPosX.text.toString().length > 0 && etPosY.text.toString().length > 0) {
                var place = Places(etPlaceName.text.toString(), etDescription.text.toString(), etTag.text.toString(), etPosX.text.toString(), etPosY.text.toString())
                db.insertDataPlaces(place)
            } else {
                Toast.makeText(context,"Please Fill All Data's",Toast.LENGTH_SHORT).show()
            }
        })

        btn_read.setOnClickListener({
            var data = db.readDataMapsPlaces()
            tvResult.text = ""
            for (i in 0..(data.size - 1)) {
                tvResult.append(data.get(i).PlacesId.toString() + " "
                        + data.get(i).PlaceName + " "
                        + data.get(i).Description + " "
                        + data.get(i).Tag + " "
                        + data.get(i).PosX + " "
                        + data.get(i).PosY + "\n")
            }
        })

        btn_update.setOnClickListener({
            val PlaceIdText = etPlaceId.text.toString()
            if (PlaceIdText.isNotEmpty() && etPlaceName.text.toString().length > 0 && etDescription.text.toString().length > 0 && etTag.text.toString().length > 0 && etPosX.text.toString().length > 0 && etPosY.text.toString().length > 0) {
                val PlaceId = PlaceIdText.toInt()
                db.updateDataPlaces(PlaceId,etPlaceName.text.toString(),etDescription.text.toString(),etTag.text.toString(),etPosX.text.toString(),etPosY.text.toString())
                btn_read.performClick()
            } else {
                Toast.makeText(context, "Please enter a valid User ID to delete", Toast.LENGTH_SHORT).show()
            }
        })

        btn_delete.setOnClickListener({
            val placeIdText = etPlaceId.text.toString()
            if (placeIdText.isNotEmpty()) {
                val placeId = placeIdText.toInt()
                db.deleteMapsData(placeId)
                btn_read.performClick()
            } else {
                Toast.makeText(context, "Please enter a valid Place ID to delete", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun goToMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}





