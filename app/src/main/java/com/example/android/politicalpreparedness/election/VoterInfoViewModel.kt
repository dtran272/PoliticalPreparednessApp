package com.example.android.politicalpreparedness.election

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionsRepository
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.network.models.Division
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

//private const val DEFAULT_REQUEST_ADDRESS = "9280 Market St. Pikesville, MD 21208"

class VoterInfoViewModel(
    private val electionsRepository: ElectionsRepository,
    private val electionId: Long,
    private val division: Division
) : ViewModel() {

    private var voterInfo: VoterInfoResponse?

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
        voterInfo = null
        _isSaved.value = null

        checkIfSaved()
        initData()
    }

    fun saveButtonClicked() {
        viewModelScope.launch {
            voterInfo?.election?.let { election ->
                if (_isSaved.value == true) {
                    electionsRepository.deleteElection(election.id)
                    _isSaved.value = false
                } else {
                    electionsRepository.saveElection(election)
                    _isSaved.value = true
                }
            }
        }
    }

    private fun checkIfSaved() {
        viewModelScope.launch {
            _isSaved.value = electionsRepository.checkElectionExist(electionId)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initData() {
        viewModelScope.launch {
            getVoterInfo()
        }
    }

    private fun getVoterInfo() {
        viewModelScope.launch {
            try {
                val addressString = "${division.state}, ${division.country}"
                voterInfo = electionsRepository.getVoterInfo(addressString, electionId)

                voterInfo?.let {
                    _electionName.value = it.election.name
                    _electionDay.value = it.election.electionDay
                    _electionInfoUrl.value = it.state?.firstOrNull()?.electionAdministrationBody?.electionInfoUrl
                    _ballotInfoUrl.value = it.state?.firstOrNull()?.electionAdministrationBody?.ballotInfoUrl
                    _address.value = it.state?.firstOrNull()?.electionAdministrationBody?.correspondenceAddress
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
