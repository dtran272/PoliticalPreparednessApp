package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionsRepository
import com.example.android.politicalpreparedness.data.network.models.Address
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class VoterInfoViewModel(private val electionsRepository: ElectionsRepository, private val electionId: Long) : ViewModel() {

    private val _electionName = MutableLiveData<String?>()
    val electionName: LiveData<String?>
        get() = _electionName

    private val _electionDay = MutableLiveData<Date?>()
    val electionDay: LiveData<Date?>
        get() = _electionDay

    private val _electionInfoUrl = MutableLiveData<String?>()
    val electionInfoUrl: LiveData<String?>
        get() = _electionInfoUrl

    private val _ballotInfoUrl = MutableLiveData<String?>()
    val ballotInfoUrl: LiveData<String?>
        get() = _ballotInfoUrl

    private val _address = MutableLiveData<Address?>()
    val address: LiveData<Address?>
        get() = _address

    private val _isSaved = MutableLiveData<Boolean?>()
    val isSaved: LiveData<Boolean?>
        get() = _isSaved

    init {
        _isSaved.value = null
        checkIfSaved()
        getVoterInfo()
    }

    private fun checkIfSaved() {
        viewModelScope.launch {
            _isSaved.value = electionsRepository.checkElectionExist(electionId)
        }
    }

    private fun getVoterInfo() {
        viewModelScope.launch {
            try {
                // TODO: replace static address with reverse geeocoding to address
                val mockAddress = "9280 Market St. Pikesville, MD 21208"

                val response = electionsRepository.getVoterInfo(mockAddress, electionId)

                _electionName.value = response.election.name
                _electionDay.value = response.election.electionDay
                _electionInfoUrl.value = response.state?.firstOrNull()?.electionAdministrationBody?.electionInfoUrl
                _ballotInfoUrl.value = response.state?.firstOrNull()?.electionAdministrationBody?.ballotInfoUrl
                _address.value = response.state?.firstOrNull()?.electionAdministrationBody?.correspondenceAddress
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}