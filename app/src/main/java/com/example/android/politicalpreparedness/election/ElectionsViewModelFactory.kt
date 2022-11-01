package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.data.ElectionsRepository

class ElectionsViewModelFactory(private val electionsRepository: ElectionsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (ElectionsViewModel(electionsRepository) as T)

}