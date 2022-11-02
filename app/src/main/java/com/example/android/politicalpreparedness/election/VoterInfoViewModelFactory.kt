package com.example.android.politicalpreparedness.election

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.PoliticalPreparednessApplication
import com.example.android.politicalpreparedness.data.ElectionsRepository

class VoterInfoViewModelFactory( private val application: Application, private val electionsRepository: ElectionsRepository, private val electionId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (VoterInfoViewModel(application, electionsRepository, electionId) as T)
}
