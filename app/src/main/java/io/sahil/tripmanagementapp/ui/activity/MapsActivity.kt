package io.sahil.tripmanagementapp.ui.activity

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import io.sahil.tripmanagementapp.R
import io.sahil.tripmanagementapp.data.TripModel
import io.sahil.tripmanagementapp.databinding.ActivityMapsBinding
import io.sahil.tripmanagementapp.ui.viewmodels.MapsViewModel
import io.sahil.tripmanagementapp.utils.MyUtils
import java.lang.Exception
import com.google.android.gms.maps.model.LatLng




class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var tripId: Long = 1
    private var tripModel: TripModel? = null
    private lateinit var mapsViewModel: MapsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tripId = intent.getLongExtra("TRIP_ID", 1)
        initToolbar()
        initViews()

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        tripModel?.let {

            val polyLineOptions = PolylineOptions()
            val routeList = arrayListOf<LatLng>()
            for(location in it.locationArray!!){
                routeList.add(LatLng(location.latitude, location.longitude))
            }
            polyLineOptions.addAll(routeList)
            polyLineOptions.width(6F)
            polyLineOptions.color(getColor(R.color.colorAccent))
            mMap.addPolyline(polyLineOptions)

            try {

                mMap.addMarker(
                    MarkerOptions()
                        .position(routeList.first())
                        .icon(BitmapDescriptorFactory.fromBitmap(MyUtils.convertToBitmap(
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_start_pin)!!, 80, 80)))
                        .title("Start")
                )

                mMap.addMarker(
                    MarkerOptions()
                        .position(routeList.last())
                        .icon(BitmapDescriptorFactory.fromBitmap(MyUtils.convertToBitmap(
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_end_pin)!!, 80, 80)))
                        .title("End")
                )

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(routeList.first(), 15F))

            }catch (e: Exception){
                Log.e("MAPS", "maps exception")
                e.printStackTrace()
            }

        }


    }

    private fun initToolbar(){
        binding.toolbar.title = "Trip $tripId"
        binding.toolbar.export = "Export"
        binding.toolbar.backButton.setOnClickListener { finish() }
        binding.toolbar.exportTrip.setOnClickListener {
            tripModel?.let {
                //export function
            } ?: Toast.makeText(applicationContext, "Loading Trip. Please Wait.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initGoogleMap(){
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initViews(){
        mapsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MapsViewModel::class.java)
        mapsViewModel.tripLiveData.observe(this, {
            it?.let {
                tripModel = it
                initGoogleMap()
            }
        })
        mapsViewModel.toastLiveData.observe(this, {
            it?.let {
                Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
            }
        })
        mapsViewModel.fetchTrip(tripId)
        binding.toolbar.exportTrip.setOnClickListener {
            //requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            export()
        }
    }

    private fun export(){
        tripModel?.let {
                model -> mapsViewModel.exportTrip(model)
        } ?: Toast.makeText(applicationContext, "Loading trip", Toast.LENGTH_SHORT).show()
    }
/*
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){ isGranted ->
        if (isGranted){
            export()
        } else {
            Toast.makeText(applicationContext, "Please provide storage permission", Toast.LENGTH_SHORT).show()
        }
    }*/

}