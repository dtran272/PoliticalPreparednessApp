package com.example.android.politicalpreparedness.data.database


import com.example.android.politicalpreparedness.data.ElectionsDataSource
import com.example.android.politicalpreparedness.data.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsLocalDataSource internal constructor(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionsDataSource {

    override suspend fun getAllElections(): List<Election> {
        return withContext(ioDispatcher) {
            electionDao.getAllElections()
        }
    }

    override suspend fun getElection(id: Long): Election? {
        return withContext(ioDispatcher) {
            electionDao.getElectionById(id)
        }
    }

    override suspend fun deleteElection(id: Long) {
        withContext(ioDispatcher) {
            electionDao.deleteElectionById(id)
        }
    }

    override suspend fun saveElection(election: Election) {
        withContext(ioDispatcher) {
            electionDao.insertElection(election)
        }
    }
}