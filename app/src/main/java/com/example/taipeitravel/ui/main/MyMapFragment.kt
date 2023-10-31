package com.example.taipeitravel.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.taipeitravel.R
import com.example.taipeitravel.databinding.FragmentMapBinding
import com.example.taipeitravel.ui.main.viewmodels.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyMapFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })

    private var _binding: FragmentMapBinding? = null
    val binding get() = _binding!!

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // reuse the view if it has not been destroyed
        if (_binding != null)
            return binding.root

        _binding = FragmentMapBinding.inflate(inflater, container, false)

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment?.getMapAsync { g ->
            googleMap = g
        }

        homeViewModel.attractions.observe(viewLifecycleOwner) {
            if (googleMap == null) return@observe
            googleMap?.clear()
            homeViewModel.attractions.value?.forEach {
                val latLng = LatLng(it.nlat, it.elong)
                val markerOptions = MarkerOptions().apply {
                    position(latLng)
                    title(it.name)
                }
                googleMap?.addMarker(markerOptions)
            }
            val first = homeViewModel.attractions.value?.firstOrNull()
            if (first != null)
                googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            first.nlat,
                            first.elong
                        ), 10f
                    )
                )

        }

        return binding.root
    }

}