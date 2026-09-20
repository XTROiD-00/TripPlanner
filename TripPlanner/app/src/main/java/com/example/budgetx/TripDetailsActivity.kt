package com.example.budgetx

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetx.data.AppDatabase
import com.example.budgetx.databinding.ActivityTripDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TripDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTripDetailsBinding
    private var tripId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tripId = intent.getLongExtra("trip_id", -1L)
        if (tripId <= 0) { finish(); return }
        binding.btnItinerary.setOnClickListener { open(ItineraryActivity::class.java) }
        binding.btnSavedPlaces.setOnClickListener { open(SavedPlacesActivity::class.java) }
        binding.btnMap.setOnClickListener { open(MapActivity::class.java) }
        binding.btnExpenses.setOnClickListener { open(HistoryActivity::class.java) }
        binding.btnAddExpense.setOnClickListener { open(AddExpenseActivity::class.java) }
        binding.btnReminders.setOnClickListener { open(RemindersActivity::class.java) }
    }

    override fun onResume() { super.onResume(); loadTrip() }

    private fun loadTrip() {
        lifecycleScope.launch {
            val trip = withContext(Dispatchers.IO) { AppDatabase.getDatabase(applicationContext).tripDao().getById(tripId) }
            if (trip == null) { Toast.makeText(this@TripDetailsActivity, "Trip not found", Toast.LENGTH_SHORT).show(); finish(); return@launch }
            binding.tvTripName.text = trip.name
            binding.tvDestination.text = trip.destination
            binding.tvDates.text = "${trip.startDate} → ${trip.endDate}"
            binding.tvNotes.text = trip.notes ?: "No notes added."
        }
    }

    private fun open(clazz: Class<*>) { startActivity(Intent(this, clazz).putExtra("trip_id", tripId)) }
}
