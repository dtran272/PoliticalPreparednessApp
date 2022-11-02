package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election

interface ElectionsDataSource {

    suspend fun getAllElections(): List<Election>

    suspend fun getElection(id: Long): Election?

    suspend fun deleteElection(id: Long)

    suspend fun saveElection(election: Election)
}