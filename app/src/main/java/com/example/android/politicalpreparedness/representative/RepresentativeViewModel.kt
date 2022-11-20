package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.network.models.Office
import com.example.android.politicalpreparedness.data.network.models.Official
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class RepresentativeViewModel() : ViewModel() {
    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val zip = MutableLiveData<String>()

    private val _state = MutableLiveData<String>()
    val state: LiveData<String>
        get() = _state

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    val showToast: SingleLiveEvent<String> = SingleLiveEvent()

    init {
        addressLine1.value = null
        addressLine2.value = null
        city.value = null
        zip.value = null
        _state.value = null
        _representatives.value = emptyList()
    }

    fun resetResults() {
        _representatives.value = emptyList()
    }

    fun searchRepresentatives() {
        val address = getAddress()

        if (address == null) {
            showToast.value = "Please leave no empty fields."
            return
        }

        viewModelScope.launch {
            try {
                val (offices, officials) = getRepresentativesResponse(address.toFormattedString())

                _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
            } catch (ex: HttpException) {
                showToast.value = "An error occurred while trying to search. Please check if address is valid."
                Timber.e(ex.message())
            }
        }
    }

    fun setSelectedState(state: String) {
        _state.value = state
    }

    fun setMyLocationAddress(myAddress: Address) {
        addressLine1.value = myAddress.line1
        addressLine2.value = myAddress.line2
        city.value = myAddress.city
        zip.value = myAddress.zip
        _state.value = myAddress.state
    }

    private fun getAddress(): Address? {
        if (addressLine1.value.isNullOrEmpty() || city.value.isNullOrEmpty() || zip.value.isNullOrEmpty() || _state.value.isNullOrEmpty()) {
            return null
        }

        return Address(
            addressLine1.value.toString(),
            addressLine2.value.toString(),
            city.value.toString(),
            _state.value.toString(),
            zip.value.toString()
        )
    }

    private suspend fun getRepresentativesResponse(addressString: String): Pair<List<Office>, List<Official>> {
        val response = CivicsApi.retrofitService.getRepresentatives(addressString)

        return Pair(response.offices, response.officials)
    }
}
