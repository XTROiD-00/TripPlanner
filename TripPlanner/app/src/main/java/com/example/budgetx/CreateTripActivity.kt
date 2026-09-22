
package com.example.budgetx

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetx.api.RetrofitClient
import com.example.budgetx.api.TripRequest
import com.example.budgetx.databinding.ActivityCreateTripBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTripBinding

    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etStartDate.setOnClickListener {
            pickDate(binding.etStartDate)
        }

        binding.etEndDate.setOnClickListener {
            pickDate(binding.etEndDate)
        }

        binding.btnSaveTrip.setOnClickListener {
            saveTrip()
        }
    }

    private fun pickDate(target: android.widget.EditText) {
        val c = Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, y, m, d ->
                c.set(y, m, d)
                target.setText(format.format(c.time))
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun saveTrip() {

        val userId = intent.getLongExtra("user_id", -1L)

        val name = binding.etTripName.text.toString().trim()
        val destination = binding.etDestination.text.toString().trim()
        val start = binding.etStartDate.text.toString().trim()
        val end = binding.etEndDate.text.toString().trim()
        val notes = binding.etNotes.text.toString().trim()

        if (
            userId <= 0 ||
            name.isEmpty() ||
            destination.isEmpty() ||
            start.isEmpty() ||
            end.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Please complete the required fields",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        lifecycleScope.launch {

            try {

                RetrofitClient.api.createTrip(
                    TripRequest(
                        name = name,
                        destination = destination,
                        start_date = start,
                        end_date = end,
                        notes = notes.ifEmpty { null }
                    )
                )

                Toast.makeText(
                    this@CreateTripActivity,
                    "Trip saved to REST API!",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } catch (e: Exception) {

                Toast.makeText(
                    this@CreateTripActivity,
                    "API error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
