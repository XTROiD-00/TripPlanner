package com.example.budgetx

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetx.data.AppDatabase
import com.example.budgetx.data.entity.Activity
import com.example.budgetx.databinding.ActivityItineraryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ItineraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItineraryBinding
    private var tripId = -1L
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItineraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tripId = intent.getLongExtra("trip_id", -1L)
        binding.etActivityDate.setOnClickListener { pickDate() }
        binding.btnAddActivity.setOnClickListener { addActivity() }
        loadActivities()
    }

    override fun onResume() { super.onResume(); if (::binding.isInitialized) loadActivities() }

    private fun pickDate() {
        val c = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d -> c.set(y,m,d); binding.etActivityDate.setText(format.format(c.time)) }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun addActivity() {
        val title = binding.etActivityTitle.text.toString().trim()
        val date = binding.etActivityDate.text.toString().trim()
        if (tripId <= 0 || title.isEmpty() || date.isEmpty()) { Toast.makeText(this, "Enter an activity title and date", Toast.LENGTH_SHORT).show(); return }
        val item = Activity(tripId = tripId, title = title, description = binding.etActivityDescription.text.toString().trim().ifEmpty { null }, date = date, startTime = binding.etStartTime.text.toString().trim().ifEmpty { null }, endTime = binding.etEndTime.text.toString().trim().ifEmpty { null }, location = binding.etLocation.text.toString().trim().ifEmpty { null })
        lifecycleScope.launch {
            withContext(Dispatchers.IO) { AppDatabase.getDatabase(applicationContext).activityDao().insert(item) }
            binding.etActivityTitle.text.clear(); binding.etActivityDescription.text.clear(); binding.etLocation.text.clear()
            loadActivities(); Toast.makeText(this@ItineraryActivity, "Activity added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadActivities() {
        lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) { AppDatabase.getDatabase(applicationContext).activityDao().getByTrip(tripId) }
            binding.activityContainer.removeAllViews()
            list.forEach { a ->
                val view = LayoutInflater.from(this@ItineraryActivity).inflate(R.layout.item_activity, binding.activityContainer, false)
                view.findViewById<TextView>(R.id.tvActivityTitle).text = a.title
                view.findViewById<TextView>(R.id.tvActivityInfo).text = buildString { append(a.date); if (!a.startTime.isNullOrEmpty()) append(" • ").append(a.startTime); if (!a.endTime.isNullOrEmpty()) append(" - ").append(a.endTime); if (!a.location.isNullOrEmpty()) append("").append(a.location) }
                view.findViewById<TextView>(R.id.tvActivityDescription).text = a.description ?: ""
                view.findViewById<CheckBox>(R.id.cbCompleted).isChecked = a.completed
                binding.activityContainer.addView(view)
            }
        }
    }
}
