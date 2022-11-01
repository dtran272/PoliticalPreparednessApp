package com.example.android.politicalpreparedness.data.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class JavaDateAdapter {
    @FromJson()
    fun javaDataFromJson(electionDay: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.parse(electionDay)
    }

    @ToJson
    fun javaDataToJson(electionDay: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(electionDay)
    }
}