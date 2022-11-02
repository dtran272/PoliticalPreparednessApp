package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionsRepository
import com.example.android.politicalpreparedness.data.network.models.Election
import kotlinx.coroutines.launch
import timber.log.Timber

class ElectionsViewModel(private val electionsRepository: ElectionsRepository) : ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    private val _navigateToSelectedElection = MutableLiveData<Election?>()
    val navigateToSelectedElection: LiveData<Election?>
        get() = _navigateToSelectedElection

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun refreshElections() {
        getUpcomingElections()
        getSavedElections()
    }

    fun displayVoterInfo(election: Election) {
        _navigateToSelectedElection.value = election
    }

    fun displayVoterInfoDone() {
        _navigateToSelectedElection.value = null
    }

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private fun getUpcomingElections() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                _upcomingElections.value = electionsRepository.getUpcomingElections()
                _isLoading.value = false
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun getSavedElections() {
        _isLoading.value = true

        viewModelScope.launch {
            _savedElections.value = electionsRepository.getSavedElections()
            _isLoading.value = false
        }
    }
}