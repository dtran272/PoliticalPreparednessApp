package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.data.ElectionsRepository

class VoterInfoViewModelFactory(private val electionsRepository: ElectionsRepository, private val electionId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (VoterInfoViewModel(electionsRepository, electionId) as T)
}
