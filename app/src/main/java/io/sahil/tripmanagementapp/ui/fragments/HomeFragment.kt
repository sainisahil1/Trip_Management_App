package io.sahil.tripmanagementapp.ui.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.sahil.tripmanagementapp.R
import io.sahil.tripmanagementapp.databinding.FragmentHomeBinding
import io.sahil.tripmanagementapp.ui.services.ForegroundService
import io.sahil.tripmanagementapp.ui.viewmodels.HomeViewModel
import io.sahil.tripmanagementapp.utils.MyPreferences
import io.sahil.tripmanagementapp.utils.TimeUtils
import java.util.jar.Manifest

class HomeFragment: Fragment() {

    private lateinit var fContext: Context
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var myPreferences: MyPreferences
    private lateinit var homeViewModel: HomeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        myPreferences = MyPreferences(fContext)
        initViews()
        registerReceiver()

        return fragmentHomeBinding.root
    }

    override fun onResume() {
        super.onResume()
        updateTripIndicator()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(fContext).unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun registerReceiver(){
        val intentFilter = IntentFilter()
        intentFilter.addAction("UPDATE_INDICATOR")
        LocalBroadcastManager.getInstance(fContext).registerReceiver(receiver, intentFilter)
    }

    private fun updateTripIndicator(){
        homeViewModel.fetchTripsCount()
        homeViewModel.fetchTotalDuration()
        Log.e(tag, "update trip indicator")
        if (myPreferences.getIsTripRunning()){
            fragmentHomeBinding.tripStatusDrawable = ContextCompat.getDrawable(fContext, R.drawable.ic_location_primary)
            fragmentHomeBinding.tripButtonText = "Stop Trip"
        } else {
            fragmentHomeBinding.tripStatusDrawable = ContextCompat.getDrawable(fContext, R.drawable.ic_location_holo_light)
            fragmentHomeBinding.tripButtonText = "Start Trip"
        }
    }

    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            when(p1?.action){
                "UPDATE_INDICATOR" -> updateTripIndicator()
            }
        }
    }

    private fun initViews(){
        fragmentHomeBinding.tripButton.setOnClickListener {
            if (myPreferences.getIsTripRunning()){
                //stop trip
                LocalBroadcastManager.getInstance(fContext).sendBroadcast(Intent("STOP_TRIP"))
            } else {
                //start trip
                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        homeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(HomeViewModel::class.java)
        homeViewModel.totalTripsLiveData.observe(viewLifecycleOwner, {
            it-> it?.let {
                fragmentHomeBinding.userStatus.totalTrips = it
            }
        })
        homeViewModel.totalDurationLiveData.observe(viewLifecycleOwner, {
            it -> it?.let {
                fragmentHomeBinding.userStatus.totalTime = TimeUtils.getTimeInHM(it)
        }
        })
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){isGranted ->
        if (isGranted){
            startForegroundService()
        } else {
            Toast.makeText(fContext, "Please provide location permission", Toast.LENGTH_SHORT).show()
        }
    }


    private fun startForegroundService(){
        val intent = Intent(fContext, ForegroundService::class.java)
        fContext.startService(intent)
    }


}