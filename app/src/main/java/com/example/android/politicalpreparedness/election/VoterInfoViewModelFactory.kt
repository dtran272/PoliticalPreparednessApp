package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.data.ElectionsRepository
import com.example.android.politicalpreparedness.data.network.models.Division

class VoterInfoViewModelFactory(
    private val electionsRepository: ElectionsRepository,
    private val electionId: Long,
    private val division: Division
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (VoterInfoViewModel(electionsRepository, electionId, division) as T)
}
