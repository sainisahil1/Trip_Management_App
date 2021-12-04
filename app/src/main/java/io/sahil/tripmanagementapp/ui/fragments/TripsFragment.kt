package io.sahil.tripmanagementapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.sahil.tripmanagementapp.R
import io.sahil.tripmanagementapp.data.TripModel
import io.sahil.tripmanagementapp.databinding.FragmentTripsBinding
import io.sahil.tripmanagementapp.io.TripIO
import io.sahil.tripmanagementapp.ui.adapters.TripListAdapter
import io.sahil.tripmanagementapp.ui.viewmodels.TripViewModel
import java.util.jar.Manifest
import kotlin.math.exp

class TripsFragment: Fragment() {

    private lateinit var fContext: Context
    private lateinit var fragmentTripsBinding: FragmentTripsBinding
    private lateinit var tripViewModel: TripViewModel
    private val tripListAdapter = TripListAdapter()
    private var tripList = listOf<TripModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fContext = context
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentTripsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trips, container, false)
        initViews()
        return fragmentTripsBinding.root
    }


    private fun initViews(){
        tripListAdapter.setContext(fContext)
        fragmentTripsBinding.tripList.layoutManager = LinearLayoutManager(fContext)
        fragmentTripsBinding.tripList.adapter = tripListAdapter

        tripViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(TripViewModel::class.java)
        tripViewModel.tripListLiveData.observe(viewLifecycleOwner, {
            it?.let {
                if (it.isEmpty()){
                    fragmentTripsBinding.emptyText.visibility = View.VISIBLE
                    fragmentTripsBinding.tripList.visibility = View.GONE
                    fragmentTripsBinding.exportButton.visibility = View.GONE
                } else {
                    tripList = it
                    fragmentTripsBinding.exportButton.visibility = View.VISIBLE
                    fragmentTripsBinding.emptyText.visibility = View.GONE
                    fragmentTripsBinding.tripList.visibility = View.VISIBLE
                    tripListAdapter.setTripList(it.asReversed()) //show by latest
                }

            }
        })
        tripViewModel.toastLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(fContext, it, Toast.LENGTH_LONG).show()
            }
        })
        fragmentTripsBinding.exportButton.setOnClickListener {
            //requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            export()
        }
    }

    override fun onResume() {
        super.onResume()
        tripViewModel.fetchTrips()
    }

    private fun export(){
        tripViewModel.exportTrips(tripList)
    }

    /*private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){ isGranted ->
        if (isGranted){
            export()
        } else {
            Toast.makeText(fContext, "Please provide storage permission", Toast.LENGTH_SHORT).show()
        }
    }*/








}