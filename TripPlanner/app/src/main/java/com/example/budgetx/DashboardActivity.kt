package com.example.budgetx

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetx.data.AppDatabase
import com.example.budgetx.data.entity.Trip
import com.example.budgetx.databinding.ActivityDashboardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("user_id", -1L)
        if (userId <= 0L) {
            Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding.btnCreateTrip.setOnClickListener {
            startActivity(Intent(this, CreateTripActivity::class.java).putExtra("user_id", userId))
        }
        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        loadTrips()
    }

    override fun onResume() {
        super.onResume()
        if (::binding.isInitialized && userId > 0L) loadTrips()
    }

    private fun loadTrips() {
        lifecycleScope.launch {
            val trips = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext).tripDao().getByUser(userId)
            }
            binding.tvTripCount.text = "${trips.size} trip(s)"
            binding.tripContainer.removeAllViews()
            if (trips.isEmpty()) {
                binding.tvEmpty.visibility = TextView.VISIBLE
            } else {
                binding.tvEmpty.visibility = TextView.GONE
                trips.forEach { addTripCard(it) }
            }
        }
    }

    private fun addTripCard(trip: Trip) {
        val card = LayoutInflater.from(this).inflate(R.layout.item_trip, binding.tripContainer, false)
        card.findViewById<TextView>(R.id.tvTripName).text = trip.name
        card.findViewById<TextView>(R.id.tvTripDestination).text = trip.destination
        card.findViewById<TextView>(R.id.tvTripDates).text = "${trip.startDate} → ${trip.endDate}"
        card.setOnClickListener {
            startActivity(Intent(this, TripDetailsActivity::class.java).putExtra("trip_id", trip.id))
        }
        binding.tripContainer.addView(card)
    }
}
