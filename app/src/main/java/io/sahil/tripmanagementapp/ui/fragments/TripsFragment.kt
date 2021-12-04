package io.sahil.tripmanagementapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
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
import java.io.File
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
                Toast.makeText(fContext, "File saved at: $it", Toast.LENGTH_LONG).show()
                val file = File(it)
                if (file.exists()){
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "application/json"
                    intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(fContext, "${fContext.packageName}.provider", file))
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Share file")
                    fContext.startActivity(Intent.createChooser(intent, "Share File"))
                }
            }
        })
        fragmentTripsBinding.exportButton.setOnClickListener {
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






}