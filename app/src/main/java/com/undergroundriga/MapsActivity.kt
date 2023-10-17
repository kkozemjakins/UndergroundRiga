package com.undergroundriga


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

internal class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val rvt = LatLng(56.95306407782407, 24.10447936633287)

        val someWhere = LatLng(55.95306407782407, 24.10447936633287)

        val MadMaxHome = LatLng(56.958674, 23.605505)

        mMap.addMarker(MarkerOptions()
            .position(someWhere)
            .title("Random Place"))

        mMap.addMarker(MarkerOptions()
            .position(rvt)
            .title("Rīgas Valsts Tehnikums"))

        mMap.addMarker(MarkerOptions()
            .position(MadMaxHome)
            .title("Maksima housik"))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rvt, 14f))


    }



}

