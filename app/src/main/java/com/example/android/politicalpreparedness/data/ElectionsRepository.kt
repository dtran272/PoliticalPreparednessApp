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
        return withContext(ioDispatcher) {
            electionsDataSource.getAllElections()
        }
    }

    override suspend fun getVoterInfo(address: String, electionId: Long): VoterInfoResponse {
        return withContext(ioDispatcher) {
            try {
                return@withContext CivicsApi.retrofitService.getVoterInfo(address, electionId)
            } catch (e: Exception) {
                Timber.e(e)
                throw e
            }
        }
    }

    override suspend fun checkElectionExist(electionId: Long): Boolean {
        return withContext(ioDispatcher) {
            val election = electionsDataSource.getElection(electionId)

            election != null
        }
    }

    override suspend fun saveElection(election: Election) {
        withContext(ioDispatcher) {
            electionsDataSource.saveElection(election)
        }
    }

    override suspend fun deleteElection(electionId: Long) {
        withContext(ioDispatcher) {
            electionsDataSource.deleteElection(electionId)
        }
    }


}