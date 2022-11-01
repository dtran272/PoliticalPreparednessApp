package com.example.android.politicalpreparedness

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.android.politicalpreparedness.data.ElectionsDataSource
import com.example.android.politicalpreparedness.data.ElectionsRepository
import com.example.android.politicalpreparedness.data.database.ElectionDatabase
import com.example.android.politicalpreparedness.data.database.ElectionsLocalDataSource

object ServiceLocator {

    private val lock = Any()
    private var database: ElectionDatabase? = null

    @Volatile
    var electionsRepository: ElectionsRepository? = null
        @VisibleForTesting set

    fun provideElectionsRepository(context: Context): ElectionsRepository {
        synchronized(this) {
            return electionsRepository ?: createElectionsRepository(context)
        }
    }

    private fun createElectionsRepository(context: Context): ElectionsRepository {
        val newRepository = ElectionsRepository(createElectionsLocalDataSource(context))

        electionsRepository = newRepository
        return newRepository
    }

    private fun createElectionsLocalDataSource(context: Context): ElectionsDataSource {
        val db = database ?: createDatabase(context)
        return ElectionsLocalDataSource(db.electionDao)
    }

    private fun createDatabase(context: Context): ElectionDatabase {
        val result = ElectionDatabase.getInstance(context)
        database = result

        return result
    }
}