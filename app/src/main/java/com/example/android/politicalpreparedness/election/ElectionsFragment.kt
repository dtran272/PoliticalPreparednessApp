package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.PoliticalPreparednessApplication
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment : Fragment() {
    private lateinit var binding: FragmentElectionBinding
    private val viewModel by viewModels<ElectionsViewModel> {
        ElectionsViewModelFactory((requireActivity().applicationContext as PoliticalPreparednessApplication).electionsRepository)
    }

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
            election.let {
                this.findNavController()
                    .navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division))

                viewModel.displayVoterInfoDone()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshElections()
    }
}