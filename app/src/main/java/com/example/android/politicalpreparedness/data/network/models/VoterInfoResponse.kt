package com.example.android.politicalpreparedness.data.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse(
    val election: Election,
    val state: List<State>? = null

    // TODO: Not needed for the time being
//    val electionElectionOfficials: List<ElectionOfficial>? = null,
//    val pollingLocations: String? = null, //TODO: Future Use
//    val contests: String? = null //TODO: Future Use
)