package com.example.taskfour.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.taskfour.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.taskfour.databinding.ActivityMapsBinding
import com.example.taskfour.model.AtmList
import com.example.taskfour.vm.MainViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //is it possible to do this or not?
        mainViewModel.getAtmList().observe(this, {
            it?.let {
                if (::mMap.isInitialized)
                    updateMap(it as AtmList)
            }
        })

        val adelaideBounds = LatLngBounds(
            LatLng(52.337240, 30.834963),  // SW bounds
            LatLng(52.557113, 31.101576) // NE bounds
        )
        mMap.setLatLngBoundsForCameraTarget(adelaideBounds)

        mMap.setMinZoomPreference(11.0f)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(52.424169, 31.014267)))
    }

    private fun updateMap(atmList: AtmList) {
        for (atm in atmList) {
            mMap.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            atm.gps_x.toDouble(),
                            atm.gps_y.toDouble()
                        )
                    )
                    .title(atm.city_type +" "+ atm.city)
                    .snippet(atm.address_type+ " "+atm.address + " "+ atm.house)
                    .icon(BitmapDescriptorFactory.defaultMarker(265F))
            )
        }
    }

}