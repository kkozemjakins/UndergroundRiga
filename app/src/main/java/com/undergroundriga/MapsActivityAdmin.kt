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
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import android.widget.ArrayAdapter
import android.widget.Spinner



class MapsActivityAdmin: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var btn_insert: Button
    private lateinit var btn_delete: Button
    private lateinit var btn_update: Button
    private lateinit var btn_read: Button
    private lateinit var etPlaceId: EditText

    private lateinit var etPlaceName: EditText
    private lateinit var etDescription : EditText
    private lateinit var spTag: Spinner
    private lateinit var etPosX: EditText
    private lateinit var etPosY: EditText

    private lateinit var tvResult: TextView

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap




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
        spTag = findViewById(R.id.spTag)
        etPosX = findViewById(R.id.etPosX)
        etPosY = findViewById(R.id.etPosY)

        tvResult = findViewById(R.id.tvResult)

        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val spinner: Spinner = findViewById(R.id.spTag)

        // Define the values for the dropdown list
        val items = arrayOf("#Teashops",
            "#Animeshops",
            "#Food",
            "#Graffity",
            "#Exotic",
            "#Second hand",
            "#Toilet",
            "#Vinyl store")

        /*
                Tags:
                "#Teashops"
                "#Animeshops"
                "#Food"
                "#Graffity"
                "#Exotic"
                "#Second hand"
                "#Toilet"
                "#Vinyl store"

                * */

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinner.adapter = adapter

        btn_insert.setOnClickListener {
            val placeName = etPlaceName.text.toString()
            val description = etDescription.text.toString()
            val posX = etPosX.text.toString()
            val posY = etPosY.text.toString()

            // Get the selected item from the Spinner
            val selectedTag = spTag.selectedItem.toString()

            if (placeName.isNotEmpty() && description.isNotEmpty() && posX.isNotEmpty() && posY.isNotEmpty()) {
                val place = Places(placeName, description, selectedTag, posX, posY)
                db.insertDataPlaces(place)
            } else {
                Toast.makeText(context, "Please Fill All Data's", Toast.LENGTH_SHORT).show()
            }
        }

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

        btn_update.setOnClickListener {
            val placeIdText = etPlaceId.text.toString()

            if (placeIdText.isNotEmpty() && etPlaceName.text.isNotEmpty() && etDescription.text.isNotEmpty() &&
                spTag.selectedItem.toString().isNotEmpty() && etPosX.text.isNotEmpty() && etPosY.text.isNotEmpty()
            ) {
                val placeId = placeIdText.toInt()
                db.updateDataPlaces(
                    placeId,
                    etPlaceName.text.toString(),
                    etDescription.text.toString(),
                    spTag.selectedItem.toString(),
                    etPosX.text.toString(),
                    etPosY.text.toString()
                )
                btn_read.performClick()
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }



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


    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        // Set up a click listener for the map
        googleMap.setOnMapClickListener { latLng ->
            // Handle the click event, you can do whatever you want with the LatLng object
            etPosX.setText(latLng.latitude.toString())
            etPosY.setText(latLng.longitude.toString())

            // Add a marker on the clicked position (optional)
            googleMap.addMarker(MarkerOptions().position(latLng).title("New Marker"))
        }
    }



    fun goToMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}





