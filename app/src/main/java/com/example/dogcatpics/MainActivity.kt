package com.example.dogcatpics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import com.example.dogcatpics.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var patchURL = ""
    var dateOfLaunch = ""
    var earliestLaunchTime: Long = Long.MAX_VALUE
    var earliestLaunchName: String? = null
    var earliestLaunchVehicleName: String? = null
    var earliestLaunchLocation: String? = null
    var earliestLaunchDescription: String? = null

    private lateinit var spaceAdapter: SpaceAdapter
    private val missionList = mutableListOf<MissionData>()

    private lateinit var rvMissions: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvMissions = binding.missionList

        // Initialize the SpaceAdapter (you can do this here) but don't set it yet
        spaceAdapter = SpaceAdapter(missionList)

        // Set a layout manager for your RecyclerView (e.g., LinearLayoutManager)
        val layoutManager = LinearLayoutManager(this)
        rvMissions.layoutManager = layoutManager

        // Attach the adapter to the RecyclerView
        rvMissions.adapter = spaceAdapter

        getJSONStrs();

    }
    private fun getJSONStrs() {
        val client = AsyncHttpClient()
        client.get("https://fdo.rocketlaunch.live/json/launches/next/5", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("Dawg", "response successful$json")
                // Clear previous mission data
                missionList.clear()

                val resultArray = json.jsonObject.getJSONArray("result")
                for (i in 0 until resultArray.length()) {
                    val launchObject = resultArray.getJSONObject(i)
                    val launchTime = launchObject.optLong("sort_date", 0)
                    val launchName = launchObject.optString("name")
                    val launchVehicle = launchObject.getJSONObject("vehicle").getString("name")
                    val launchLocale = launchObject.getJSONObject("pad").getJSONObject("location").getString("name")
                    val launchDescription = launchObject.getString("launch_description")

                    // Create a MissionData object and add it to the list
                    val missionData = MissionData(
                        name = launchName,
                        date = launchTime,
                        vehicle = launchVehicle,
                        location = launchLocale,
                        description = launchDescription
                    )
                    missionList.add(missionData)
                }
                // Initialize the SpaceAdapter and set it as the adapter for the RecyclerView
                spaceAdapter = SpaceAdapter(missionList)
                rvMissions.adapter = spaceAdapter

                // Notify the adapter that the data has changed
                spaceAdapter.notifyDataSetChanged()
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.d("Dog Error", errorResponse)
            }
        })
    }

}