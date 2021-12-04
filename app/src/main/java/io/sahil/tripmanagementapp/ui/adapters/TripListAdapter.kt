package io.sahil.tripmanagementapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.sahil.tripmanagementapp.R
import io.sahil.tripmanagementapp.databinding.LayoutTripListItemBinding
import io.sahil.tripmanagementapp.data.TripModel
import io.sahil.tripmanagementapp.ui.activity.MapsActivity

class TripListAdapter: RecyclerView.Adapter<TripListAdapter.ViewHolder>() {

    private var context: Context? = null

    class ViewHolder(private val layoutTripListItemBinding: LayoutTripListItemBinding, private val context: Context?): RecyclerView.ViewHolder(layoutTripListItemBinding.root){
        fun bind(tripModel: TripModel){
            layoutTripListItemBinding.trip = tripModel
            layoutTripListItemBinding.tripCard.setOnClickListener {
                context?.let {
                    val intent = Intent(context, MapsActivity::class.java)
                    intent.putExtra("TRIP_ID", tripModel.tripId)
                    it.startActivity(intent)
                }
            }
        }
    }

    private var tripList = listOf<TripModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutTripListItemBinding: LayoutTripListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.layout_trip_list_item, parent, false)

        return ViewHolder(layoutTripListItemBinding, context)
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