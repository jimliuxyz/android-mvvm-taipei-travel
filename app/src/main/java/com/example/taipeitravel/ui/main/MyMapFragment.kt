package com.example.taipeitravel.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.taipeitravel.R
import com.example.taipeitravel.databinding.FragmentMapBinding
import com.example.taipeitravel.ui.main.viewmodels.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MyMapFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })

    private var _binding: FragmentMapBinding? = null
    val binding get() = _binding!!

    private var googleMap: GoogleMap? = null

    private val markers = ArrayList<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // observe attractions changes
        homeViewModel.attractions.observe(this) {
            if (googleMap == null) return@observe
            googleMap?.clear()
            markers.clear()
            homeViewModel.attractions.value?.forEach {
                val latLng = LatLng(it.nlat, it.elong)
                val markerOptions = MarkerOptions().apply {
                    position(latLng)
                    title(it.name)
                }
                var marker = googleMap?.addMarker(markerOptions)
                if (marker != null)
                    markers.add(marker)
            }
            animateCamera(0, 13f)
        }

        // listen animate map event
        lifecycleScope.launch {
            homeViewModel.animateMapEvent.collectLatest { seekToIdx ->
                animateCamera(seekToIdx, null)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        // get googleMap instance
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment?.getMapAsync { g ->
            googleMap = g
            g.setOnMarkerClickListener { marker ->
                navToDetail(marker)
                return@setOnMarkerClickListener false
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        googleMap?.setOnMarkerClickListener(null)
        googleMap = null
        _binding = null
    }

    private fun navToDetail(marker: Marker) {
        val idx = homeViewModel.attractions.value?.indexOfFirst { it.name == marker.title }
        if (idx != null)
            homeViewModel.emitNavToDetail(idx)
    }

    private fun animateCamera(idx: Int, zoom: Float?) {
        if (idx < 0 && idx >= homeViewModel.attractions.value!!.size)
            return

        var marker = markers[idx]
        marker.showInfoWindow()
        if (zoom != null) {
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        marker.position.latitude,
                        marker.position.longitude
                    ), zoom
                )
            )
        } else {
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        marker.position.latitude,
                        marker.position.longitude
                    )
                )
            )
        }
    }

}