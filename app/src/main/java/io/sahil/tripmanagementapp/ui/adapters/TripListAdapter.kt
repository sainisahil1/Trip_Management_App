package io.sahil.tripmanagementapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.sahil.tripmanagementapp.R
import io.sahil.tripmanagementapp.databinding.LayoutTripListItemBinding
import io.sahil.tripmanagementapp.data.TripModel

class TripListAdapter: RecyclerView.Adapter<TripListAdapter.ViewHolder>() {

    private var context: Context? = null

    class ViewHolder(private val layoutTripListItemBinding: LayoutTripListItemBinding): RecyclerView.ViewHolder(layoutTripListItemBinding.root){
        fun bind(tripModel: TripModel){
            layoutTripListItemBinding.trip = tripModel
        }
        //add maps activity intent
    }

    private var tripList = listOf<TripModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutTripListItemBinding: LayoutTripListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.layout_trip_list_item, parent, false)

        return ViewHolder(layoutTripListItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tripList[position])
    }

    override fun getItemCount(): Int {
        return tripList.size
    }

    fun setTripList(list: List<TripModel>){
        tripList = list
        notifyDataSetChanged()
    }

    fun setContext(context: Context){
        this.context = context
    }



}