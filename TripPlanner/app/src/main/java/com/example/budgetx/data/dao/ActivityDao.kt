package com.example.budgetx.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetx.data.entity.Activity

@Dao
interface ActivityDao {
    @Insert suspend fun insert(activity: Activity): Long
    @Query("SELECT * FROM activities WHERE tripId = :tripId ORDER BY date, startTime") suspend fun getByTrip(tripId: Long): List<Activity>
}
