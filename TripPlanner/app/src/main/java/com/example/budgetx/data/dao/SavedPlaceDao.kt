package com.example.budgetx.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetx.data.entity.SavedPlace

@Dao
interface SavedPlaceDao {
    @Insert suspend fun insert(place: SavedPlace): Long
    @Query("SELECT * FROM saved_places WHERE tripId = :tripId ORDER BY name") suspend fun getByTrip(tripId: Long): List<SavedPlace>
}
