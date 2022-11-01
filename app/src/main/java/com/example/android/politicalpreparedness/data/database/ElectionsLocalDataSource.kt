package com.example.android.politicalpreparedness.data.database


import com.example.android.politicalpreparedness.data.ElectionsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ElectionsLocalDataSource internal constructor(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionsDataSource {
}