package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.models.Election
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
            }
            emptyList<Election>()
        }
    }

    override suspend fun getSavedElections(): List<Election> {
        // TODO
        return emptyList()
    }

}