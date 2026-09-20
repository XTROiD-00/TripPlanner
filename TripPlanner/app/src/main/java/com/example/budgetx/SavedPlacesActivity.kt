package com.example.budgetx

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetx.data.AppDatabase
import com.example.budgetx.data.entity.SavedPlace
import com.example.budgetx.databinding.ActivitySavedPlacesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedPlacesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedPlacesBinding
    private var tripId = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); binding = ActivitySavedPlacesBinding.inflate(layoutInflater); setContentView(binding.root)
        tripId = intent.getLongExtra("trip_id", -1L)
        binding.btnSavePlace.setOnClickListener { savePlace() }
        loadPlaces()
    }
    override fun onResume() { super.onResume(); if (::binding.isInitialized) loadPlaces() }
    private fun savePlace() {
        val name = binding.etPlaceName.text.toString().trim(); val address = binding.etPlaceAddress.text.toString().trim(); val notes = binding.etPlaceNotes.text.toString().trim()
        if (tripId <= 0 || name.isEmpty()) { Toast.makeText(this, "Enter a place name", Toast.LENGTH_SHORT).show(); return }
        lifecycleScope.launch {
            withContext(Dispatchers.IO) { AppDatabase.getDatabase(applicationContext).savedPlaceDao().insert(SavedPlace(tripId = tripId, name = name, address = address.ifEmpty { null }, notes = notes.ifEmpty { null })) }
            binding.etPlaceName.text.clear(); binding.etPlaceAddress.text.clear(); binding.etPlaceNotes.text.clear(); loadPlaces(); Toast.makeText(this@SavedPlacesActivity, "Place saved", Toast.LENGTH_SHORT).show()
        }
    }
    private fun loadPlaces() {
        lifecycleScope.launch {
            val places = withContext(Dispatchers.IO) { AppDatabase.getDatabase(applicationContext).savedPlaceDao().getByTrip(tripId) }
            binding.placeContainer.removeAllViews()
            places.forEach { p ->
                val v = LayoutInflater.from(this@SavedPlacesActivity).inflate(R.layout.item_place, binding.placeContainer, false)
                v.findViewById<TextView>(R.id.tvPlaceName).text = p.name
                v.findViewById<TextView>(R.id.tvPlaceAddress).text = p.address ?: "No address"
                v.findViewById<TextView>(R.id.tvPlaceNotes).text = p.notes ?: ""
                binding.placeContainer.addView(v)
            }
        }
    }
}
