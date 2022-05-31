package com.upt.cleancity.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.upt.cleancity.R
import com.upt.cleancity.model.Issue
import com.upt.cleancity.service.IssueService
import com.upt.cleancity.service.factory.IssueServiceFactory
import com.upt.cleancity.utils.AppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var mapFragment: SupportMapFragment

    private lateinit var issueService: IssueService
    private var loggedInUser = AppState.loggedInUser

    private var issueMarkerMap = mutableMapOf<String, Marker>()

    companion object {
        const val TAG = "__MapsActivity"
        const val ADD_MARKER = 200
        const val DELETE_MARKER = 201
        const val VIEW_ISSUE = 203
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        issueService = IssueServiceFactory.makeService(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapClickListener(this)
        mMap.setOnMarkerClickListener(this)

        fetchLastLocation()
        loadAllIssuesAndMarkers()
    }

    override fun onMapClick(latLng: LatLng) {
        val intent = Intent(this, CreateIssueActivity::class.java)
        intent.putExtra("MARKER_LATITUDE", latLng.latitude)
        intent.putExtra("MARKER_LONGITUDE", latLng.longitude)
        startActivityForResult(intent, ADD_MARKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            ADD_MARKER -> {
                val issueId = data!!.getStringExtra("ISSUE_ID")
                val latitude = data.getDoubleExtra("ISSUE_LATITUDE", 0.0)
                val longitude = data.getDoubleExtra("ISSUE_LONGITUDE", 0.0)
                addMarkerForIssue(latitude, longitude, issueId!!)
            }
            DELETE_MARKER -> {
                val issueId = data!!.getStringExtra("ISSUE_ID")
                val marker = issueMarkerMap[issueId]
                marker!!.remove()
                issueMarkerMap.remove(issueId)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadAllIssuesAndMarkers() {
        issueService.getAllIssues().enqueue(object : Callback<List<Issue>> {
            override fun onResponse(call: Call<List<Issue>>, response: Response<List<Issue>>) {
                Log.d(TAG, "getAllIssues: onResponse()")
                if (response.code() == 200) {
                    val issues = response.body()!!
                    loadExistingIssueMarkers(issues)
                } else {
                    Log.w(TAG, "Error code: " + response.code())
                    Toast.makeText(this@MapsActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Issue>>, t: Throwable) {
                Log.w(TAG, "getAllIssues: onFailure()", t)
                Toast.makeText(this@MapsActivity, "Failed to retrieve issues", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun loadExistingIssueMarkers(issues: List<Issue>) {
        issues.forEach {
            addMarkerForIssue(it.lat, it.long, it.id!!)
        }
    }

    private fun addMarkerForIssue(latitude: Double, longitude: Double, issueId: String) {
        val marker = mMap.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title(issueId))
        if (marker != null) {
            issueMarkerMap[issueId] = marker
        }
    }

    private fun fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                updateCurrentLocation()
            }
        }
    }

    private fun updateCurrentLocation() {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12F))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            101 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation()
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val intent = Intent(this, ViewIssueActivity::class.java)
        intent.putExtra("ISSUE_ID", marker.title!!)
        startActivityForResult(intent, VIEW_ISSUE)
        return true
    }
}