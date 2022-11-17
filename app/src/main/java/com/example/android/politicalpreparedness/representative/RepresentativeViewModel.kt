package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.data.network.models.Address

class RepresentativeViewModel : ViewModel() {

    private var statesArray: Array<String>

    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val zip = MutableLiveData<String>()

    private val _selectedStatePosition = MutableLiveData<Int>()
    val selectedStatePosition: LiveData<Int>
        get() = _selectedStatePosition

    init {
        addressLine1.value = null
        addressLine2.value = null
        city.value = null
        zip.value = null
        statesArray = emptyArray()
    }

    fun setStatesArray(data: Array<String>) {
        statesArray = data
    }

    fun setSelectedStatePosition(position: Int) {
        _selectedStatePosition.value = position
    }

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location
    fun setMyLocationAddress(myAddress: Address) {
        addressLine1.value = myAddress.line1
        addressLine2.value = myAddress.line2
        city.value = myAddress.city
        zip.value = myAddress.zip
        _selectedStatePosition.value = statesArray.indexOf(myAddress.state)
    }

    //TODO: Create function to get address from individual fields
}
