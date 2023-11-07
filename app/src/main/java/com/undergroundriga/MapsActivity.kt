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

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var vMenu: LinearLayout
    private lateinit var bHideMenu: Button
    private lateinit var userTV: TextView
    private lateinit var logoutBtn: Button

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
    }

    private fun setupButtonListeners() {
        val myButton = findViewById<Button>(R.id.myButton)
        val buttonHideMenu = findViewById<Button>(R.id.bHideMenu)

        myButton.setOnClickListener {
            toggleMenuWidth()
        }

        buttonHideMenu.setOnClickListener {
            toggleHideMenuWidth()
        }
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

            if (places == data.last()) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapPoint, 14f))
            }
        }
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
