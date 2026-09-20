package com.example.budgetx.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetx.data.entity.Reminder

@Dao
interface ReminderDao {
    @Insert suspend fun insert(reminder: Reminder): Long
    @Query("SELECT * FROM reminders WHERE tripId = :tripId ORDER BY reminderDateTime") suspend fun getByTrip(tripId: Long): List<Reminder>
}
