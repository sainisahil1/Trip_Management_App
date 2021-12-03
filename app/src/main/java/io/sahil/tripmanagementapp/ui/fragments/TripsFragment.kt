package io.sahil.tripmanagementapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.sahil.tripmanagementapp.R
import io.sahil.tripmanagementapp.databinding.FragmentTripsBinding
import io.sahil.tripmanagementapp.ui.adapters.TripListAdapter
import io.sahil.tripmanagementapp.ui.viewmodels.TripViewModel

class TripsFragment: Fragment() {

    private lateinit var fContext: Context
    private lateinit var fragmentTripsBinding: FragmentTripsBinding
    private lateinit var tripViewModel: TripViewModel
    private val tripListAdapter = TripListAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fContext = context
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
                } else {
                    fragmentTripsBinding.emptyText.visibility = View.GONE
                    fragmentTripsBinding.tripList.visibility = View.VISIBLE
                    tripListAdapter.setTripList(it.asReversed()) //show by latest
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()
        tripViewModel.fetchTrips()
    }




}