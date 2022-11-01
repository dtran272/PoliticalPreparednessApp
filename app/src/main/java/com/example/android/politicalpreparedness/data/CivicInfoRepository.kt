package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election

interface CivicInfoRepository {

    suspend fun getUpcomingElections(): List<Election>

    suspend fun getSavedElections(): List<Election>
}