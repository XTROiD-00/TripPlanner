package com.example.budgetx.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetx.data.entity.Trip

@Dao
interface TripDao {
    @Insert suspend fun insert(trip: Trip): Long
    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY startDate") suspend fun getByUser(userId: Long): List<Trip>
    @Query("SELECT * FROM trips WHERE id = :tripId LIMIT 1") suspend fun getById(tripId: Long): Trip?
}
