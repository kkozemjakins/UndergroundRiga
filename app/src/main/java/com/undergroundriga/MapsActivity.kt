package com.undergroundriga

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.widget.TextView
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.content.SharedPreferences
import android.location.Location
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.util.Log

import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import android.os.Looper
import com.google.android.gms.maps.model.Marker


import android.content.pm.PackageManager
import android.Manifest
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.location.LocationListener
import android.widget.ImageButton


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {


    private lateinit var mMap: GoogleMap
    private lateinit var vMenu: LinearLayout
    private lateinit var bHideMenu: Button
    private lateinit var userTV: TextView
    private lateinit var logoutBtn: Button

    private val YOUR_PERMISSION_REQUEST_CODE = 123 // Use any unique integer value


    lateinit var sharedPreferences: SharedPreferences
    var PREFS_KEY = "prefs"
    var USER_KEY = "user"
    var usr = ""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        userTV = findViewById(R.id.idTVUserName)
        logoutBtn = findViewById(R.id.idBtnLogOut)

        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        usr = sharedPreferences.getString(USER_KEY, null)!!

        userTV.setText("Welcome, $usr !")

        logoutBtn.setOnClickListener {

            // on below line we are creating a variable for
            // editor of shared preferences and initializing it.
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            // on below line we are clearing our editor.
            editor.clear()

            // on below line we are applying changes which are cleared.
            editor.apply()

            // on below line we are opening our mainactivity by calling intent
            val i = Intent(this@MapsActivity, MainActivity::class.java)

            // on below line we are simply starting
            // our activity to start main activity
            startActivity(i)

            // on below line we are calling
            // finish to close our main activity.
            finish()
        }


        initializeViews()
        setupButtonListeners()
    }

    private fun initializeViews() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        vMenu = findViewById(R.id.vMenu)
        bHideMenu = findViewById(R.id.bHideMenu)
        val btnFocusOnLocation = findViewById<ImageButton>(R.id.btnFocusOnLocation)

        btnFocusOnLocation.setOnClickListener {
            focusOnUserLocation()
        }
    }

    private fun setupButtonListeners() {
        val myButton = findViewById<Button>(R.id.btnMenu)
        val buttonHideMenu = findViewById<Button>(R.id.bHideMenu)

        myButton.setOnClickListener {
            toggleMenuWidth()
        }

        buttonHideMenu.setOnClickListener {
            toggleHideMenuWidth()
        }
    }


    override fun onLocationChanged(location: Location) {
        updateLocationOnMap(location)
    }


    private fun focusOnUserLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
                    } else {
                        Toast.makeText(this, "Current location is null", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e: Exception ->
                    Toast.makeText(
                        this,
                        "Failed to get current location: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            // Permission is not granted, request the permission from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                YOUR_PERMISSION_REQUEST_CODE
            )
        }
    }



    private var CurrentLocationMarker: Marker? = null
    private fun updateLocationOnMap(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)

        // Remove the last location marker if it exists
        CurrentLocationMarker?.remove()

        // Add the new location marker to the map
        CurrentLocationMarker = mMap.addMarker(
            MarkerOptions()
                .position(currentLatLng)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location))
        )

        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val db = DataBaseHandler(this)
        val data = db.readDataMapsPlaces()

        data.forEach { places ->
            val mapPoint = LatLng(places.PosX.toDouble(), places.PosY.toDouble())



            mMap.addMarker(
                MarkerOptions()
                    .position(mapPoint)
                    .title(places.PlaceName)
                    .snippet("${places.Description}_${places.Tag}")
            )
            /*
                        if (places == data.last()) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapPoint, 14f))
                        }*/
        }



        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Check if the app has the necessary location permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is granted, proceed with getting the last location
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)

                        CurrentLocationMarker = mMap.addMarker(
                            MarkerOptions()
                                .position(currentLatLng)
                                .title("Your Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location))
                        )
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f));

                       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))
                    } else {
                        Toast.makeText(this, "Current location is null", Toast.LENGTH_SHORT).show()

                    }
                }
                .addOnFailureListener { e: Exception ->
                    Toast.makeText(this, "Failed to get current location: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        } else {
            // Permission is not granted, request the permission from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                YOUR_PERMISSION_REQUEST_CODE
            )
        }

        // Request location updates
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5000) // Update location every 5 seconds

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult?.lastLocation?.let { location ->
                        updateLocationOnMap(location)
                    }
                }
            },
            Looper.getMainLooper()
        )


    }

    private fun toggleMenuWidth() {
        val layoutParams = vMenu.layoutParams
        val layoutParamsB = bHideMenu.layoutParams

        if (layoutParams.width == 0 && layoutParamsB.width == 0) {
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.menu_width)
            layoutParamsB.width = resources.getDimensionPixelSize(R.dimen.button_menu_width_hide)
        } else {
            layoutParams.width = 0
            layoutParamsB.width = 0
        }

        vMenu.layoutParams = layoutParams
        bHideMenu.layoutParams = layoutParamsB
    }

    private fun toggleHideMenuWidth() {
        val layoutParams = vMenu.layoutParams
        val layoutParamsB = bHideMenu.layoutParams

        if (layoutParams.width == resources.getDimensionPixelSize(R.dimen.menu_width) &&
            layoutParamsB.width == resources.getDimensionPixelSize(R.dimen.button_menu_width_hide)) {
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.menu_width_hide)
            layoutParamsB.width = resources.getDimensionPixelSize(R.dimen.hide_button_menu_width_hide)
        } else {
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.menu_width)
            layoutParamsB.width = resources.getDimensionPixelSize(R.dimen.button_menu_width_hide)
        }

        vMenu.layoutParams = layoutParams
        bHideMenu.layoutParams = layoutParamsB
    }


}
