package com.example.budgetx.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.budgetx.data.dao.*
import com.example.budgetx.data.entity.*

@Database(
    entities = [User::class, Trip::class, Activity::class, SavedPlace::class, Expense::class, Reminder::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    abstract fun activityDao(): ActivityDao
    abstract fun savedPlaceDao(): SavedPlaceDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "trip_planner_database")
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}
