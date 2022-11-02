package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse

interface CivicInfoRepository {

    suspend fun getUpcomingElections(): List<Election>

    suspend fun getSavedElections(): List<Election>

    suspend fun getVoterInfo(address: String, electionId: Long): VoterInfoResponse

    suspend fun checkElectionExist(electionId: Long): Boolean
}