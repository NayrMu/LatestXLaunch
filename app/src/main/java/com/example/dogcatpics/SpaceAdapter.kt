package com.example.dogcatpics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date

class SpaceAdapter(private val missionList: List<MissionData>) : RecyclerView.Adapter<SpaceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val logo: ImageView
        val mName: TextView
        val mDate: TextView
        val mVehicle: TextView
        val mLocale: TextView
        val mDescription: TextView

        init {
            logo = view.findViewById(R.id.patchImage)
            mName = view.findViewById(R.id.missionNameText)
            mDate = view.findViewById(R.id.dateText)
            mVehicle = view.findViewById(R.id.launchVehicleText)
            mLocale = view.findViewById(R.id.launchLocaleText)
            mDescription = view.findViewById(R.id.launchDescriptionText)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mission_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mission = missionList[position]

        holder.mName.text = mission.name
        holder.mDate.text = unixTimeToDateString(mission.date)
        holder.mVehicle.text = mission.vehicle
        holder.mLocale.text = mission.location
        holder.mDescription.text = mission.description

        // Load the logo if you have a URL for it
        // Glide.with(holder.itemView)
        //     .load(mission.logoUrl)
        //     .centerCrop()
        //     .into(holder.logo)
    }

    override fun getItemCount() = missionList.size

    private fun unixTimeToDateString(unixTime: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(unixTime * 1000)
        return sdf.format(date)
    }
}

