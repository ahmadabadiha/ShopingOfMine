package com.example.shopingofmine.ui.map

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.carto.styles.AnimationStyle
import com.carto.styles.AnimationStyleBuilder
import com.carto.styles.AnimationType
import com.carto.styles.MarkerStyleBuilder
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentMapBinding
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.MapView
import org.neshan.mapsdk.internal.utils.BitmapUtils
import org.neshan.mapsdk.model.Marker

class MapFragment : Fragment(R.layout.fragment_map) {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: MapView
    private lateinit var animSt: AnimationStyle
    private lateinit var marker: Marker

    private var isLocationSelected = false

    private var lat: Double? = null
    private var lon: Double? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMapBinding.bind(view)

    }

    override fun onStart() {
        super.onStart()
        initLayoutReferences()
    }

    private fun initLayoutReferences() {
        initViews()

        map.setOnMapClickListener {
            lat = it.latitude
            lon = it.longitude
            map.addMarker(createMarker(it))
            isLocationSelected = true
        }
        binding.selectLocation.setOnClickListener {
            if (isLocationSelected){
                setFragmentResult("coordinates", bundleOf("lat" to lat, "lon" to lon))
                findNavController().navigate(MapFragmentDirections.actionMapFragmentToLoginFragment())
            } else Toast.makeText(requireContext(), "لطفا با کلیک کردن روی نقشه موقعیت مکانی خود را تعیین کنید.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews() {
        map = binding.mapview
    }

    private fun createMarker(loc: LatLng): Marker {

        if (isLocationSelected) map.removeMarker(marker)
        val animStBl = AnimationStyleBuilder()
        animStBl.fadeAnimationType = AnimationType.ANIMATION_TYPE_SMOOTHSTEP
        animStBl.sizeAnimationType = AnimationType.ANIMATION_TYPE_SPRING
        animStBl.phaseInDuration = 0.5f
        animStBl.phaseOutDuration = 0.5f
        animSt = animStBl.buildStyle()

        val markStCr = MarkerStyleBuilder()
        markStCr.size = 30f
        markStCr.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(
            BitmapFactory.decodeResource(
                resources, R.drawable.ic_marker
            )
        )
        markStCr.animationStyle = animSt
        val markSt = markStCr.buildStyle()
        marker = Marker(loc, markSt)
        return marker
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}