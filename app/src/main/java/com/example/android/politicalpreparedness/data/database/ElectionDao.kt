package com.example.android.politicalpreparedness.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.data.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(elections: Election)

    @Query("select * from election_table order by electionDay")
    fun getAllElections(): List<Election>

    @Query("select * from election_table where id =:electionId")
    fun getElectionById(electionId: Long): Election?

    @Query("delete from election_table where id =:electionId")
    fun deleteElectionById(electionId: Long)

    @Query("delete from election_table")
    fun clearAll()

}