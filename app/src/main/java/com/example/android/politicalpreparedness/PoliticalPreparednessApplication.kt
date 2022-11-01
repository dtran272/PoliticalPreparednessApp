package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.data.ElectionsRepository
import timber.log.Timber

class PoliticalPreparednessApplication : Application() {

    val electionsRepository: ElectionsRepository
        get() = ServiceLocator.provideElectionsRepository(this)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}