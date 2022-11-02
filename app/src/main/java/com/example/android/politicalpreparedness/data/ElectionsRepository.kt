package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ElectionsRepository(
    private val electionsDataSource: ElectionsDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CivicInfoRepository {

    override suspend fun getUpcomingElections(): List<Election> {
        return withContext(ioDispatcher) {
            try {
                val electionsResponse = CivicsApi.retrofitService.getElections()

                return@withContext electionsResponse.elections
            } catch (e: Exception) {
                Timber.e(e)
                throw e
            }
        }
    }

    override suspend fun getSavedElections(): List<Election> {
        // TODO: Get saved elections from DB
        return emptyList()
    }

    override suspend fun getVoterInfo(address: String, electionId: Long): VoterInfoResponse {
        return withContext(ioDispatcher) {
            try {
                val response = CivicsApi.retrofitService.getVoterInfo(address, electionId)
                return@withContext response
            } catch (e: Exception) {
                Timber.e(e)
                throw e
            }
        }
    }

    override suspend fun checkElectionExist(electionId: Long): Boolean {
        // TODO: Check election by in DB
        return false
    }

}