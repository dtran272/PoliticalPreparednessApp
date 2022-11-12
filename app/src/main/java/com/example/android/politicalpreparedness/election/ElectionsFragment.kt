package com.example.android.politicalpreparedness.election

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.PoliticalPreparednessApplication
import com.example.android.politicalpreparedness.data.network.models.Division
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar

// NOTE: We are requesting the location permission in case where the Elections api request only does not return a complete "ocdDivisionId".
// Which includes the State and Country, for us to make the VoterInfo api request, in the VoterInfoFragment.
const val REQUEST_LOCATION_PERMISSION = 1

private const val DEFAULT_COUNTRY = "US"
private const val DEFAULT_STATE = "CA"

class ElectionsFragment : Fragment() {
    private lateinit var binding: FragmentElectionBinding
    private val viewModel by viewModels<ElectionsViewModel> {
        ElectionsViewModelFactory((requireActivity().applicationContext as PoliticalPreparednessApplication).electionsRepository)
    }

    private var currentLocationDivision: Division? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.upcomingElectionsRecyclerView.adapter = ElectionListAdapter(ElectionListener {
            viewModel.displayVoterInfo(it)
        })

        binding.savedElectionsRecyclerView.adapter = ElectionListAdapter(ElectionListener {
            viewModel.displayVoterInfo(it)
        })

        viewModel.navigateToSelectedElection.observe(viewLifecycleOwner) { election ->
            election?.let {
                val divisionArg = getValidDivisionData(it.division)

                this.findNavController()
                    .navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it.id, divisionArg))

                viewModel.displayVoterInfoDone()
            }
        }

        return binding.root
    }

    private fun getValidDivisionData(division: Division): Division {
        if (division.country.isNotEmpty() && division.state.isNotEmpty()) {
            return division
        }

        var result = Division(division.id, DEFAULT_COUNTRY, DEFAULT_STATE)

        if (isPermissionGranted() && currentLocationDivision != null) {
            currentLocationDivision?.let {
                result = Division(division.id, it.country, it.state)
            }
        }

        return result
    }

    override fun onStart() {
        super.onStart()
        requestLocationPermission()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshElections()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            Snackbar.make(binding.root, "Permission granted!", Snackbar.LENGTH_SHORT).show()
            getLocationDivisionData()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationDivisionData() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation?.addOnCompleteListener() { task ->
            if (task.isSuccessful && task.result != null) {
                var country = ""
                var state = ""
                val lastLocation = task.result

                if (lastLocation != null) {
                    val addressResult = Geocoder(requireContext()).getFromLocation(lastLocation.latitude, lastLocation.longitude, 1)

                    addressResult.firstOrNull()?.let {
                        country = it.countryCode
                        state = it.adminArea
                    }
                }

                currentLocationDivision = Division("", country, state)
            }
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        if (isPermissionGranted()) {
            getLocationDivisionData()
            return
        }

        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }
}