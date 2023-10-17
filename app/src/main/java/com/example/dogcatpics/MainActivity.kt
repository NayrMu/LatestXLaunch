package com.example.dogcatpics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var button = binding.randomButton;
        var imageView = binding.patchImage;
        refresh(button, imageView);
        getJSONStrs();

    }
    private fun getJSONStrs() {
        val client = AsyncHttpClient()
        client["https://fdo.rocketlaunch.live/json/launches/next/5", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Dawg", "response successful$json")
                // Iterate through the array to find the earliest launch
                val resultArray = json.jsonObject.getJSONArray("result")
                for (i in 0 until resultArray.length()) {
                    val launchObject = resultArray.getJSONObject(i)
                    val launchTime = launchObject.optLong("sort_date", 0)
                    val launchName = launchObject.optString("name")
                    val launchVehicle = launchObject.getJSONObject("vehicle").getString("name")
                    val launchLocale = launchObject.getJSONObject("pad").getJSONObject("location").getString("name")
                    val launchDescription = launchObject.getString("launch_description")
                    if (launchTime < earliestLaunchTime) {
                        earliestLaunchTime = launchTime
                        earliestLaunchName = launchName
                        earliestLaunchVehicleName = launchVehicle
                        earliestLaunchLocation = launchLocale
                        earliestLaunchDescription = launchDescription
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Dog Error", errorResponse)
            }
        }]
    }
    private fun refresh(button: Button, imageView: ImageView) {
        button.setOnClickListener {
            getJSONStrs()
            binding.missionNameText.setText(earliestLaunchName)
            binding.dateText.setText(unixTimeToDateString(earliestLaunchTime))
            binding.launchVehicleText.setText(earliestLaunchVehicleName)
            binding.launchLocaleText.setText(earliestLaunchLocation)
            binding.launchDescriptionText.setText(earliestLaunchDescription)
        }
    }
    fun unixTimeToDateString(unixTime: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(unixTime * 1000)
        return sdf.format(date)
    }

}