package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.PoliticalPreparednessApplication
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val electionId = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId

        val viewModelFactory = VoterInfoViewModelFactory(
            (requireActivity().applicationContext as PoliticalPreparednessApplication).electionsRepository,
            electionId = electionId
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.electionInfoUrl.observe(viewLifecycleOwner, Observer { url ->
            url?.let {
                configureLoadUrl(binding.electionInfo, it)
            }
        })

        viewModel.ballotInfoUrl.observe(viewLifecycleOwner, Observer { url ->
            url?.let {
                configureLoadUrl(binding.ballotInfo, it)
            }
        })

        binding.saveButton.setOnClickListener {
            viewModel.saveButtonClicked()
        }

        return binding.root
    }

    //TODO: Create method to load URL intents
    private fun configureLoadUrl(textView: TextView, url: String) {
        textView.setOnClickListener {
            val loadUrlIntent = Intent(Intent.ACTION_VIEW)
            loadUrlIntent.data = Uri.parse(url)

            startActivity(loadUrlIntent)
        }
    }
}