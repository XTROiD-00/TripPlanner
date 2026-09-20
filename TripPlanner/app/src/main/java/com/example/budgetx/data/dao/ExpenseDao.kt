package com.example.budgetx.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetx.data.entity.Expense

@Dao
interface ExpenseDao {
    @Insert suspend fun insert(expense: Expense): Long
    @Delete suspend fun delete(expense: Expense)
    @Query("SELECT * FROM expenses WHERE tripId = :tripId ORDER BY date DESC") suspend fun getByTrip(tripId: Long): List<Expense>
    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE tripId = :tripId") suspend fun getTotal(tripId: Long): Double
}
