package com.example.budgetx

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetx.data.AppDatabase
import com.example.budgetx.databinding.ActivityHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private var tripId = -1L
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); binding = ActivityHistoryBinding.inflate(layoutInflater); setContentView(binding.root); tripId = intent.getLongExtra("trip_id", -1L); loadExpenses() }
    override fun onResume() { super.onResume(); if (::binding.isInitialized) loadExpenses() }
    private fun loadExpenses() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val expenses = withContext(Dispatchers.IO) { db.expenseDao().getByTrip(tripId) }
            val total = withContext(Dispatchers.IO) { db.expenseDao().getTotal(tripId) }
            binding.tvTotal.text = "Total: R ${String.format("%.2f", total)}"
            binding.expenseContainer.removeAllViews()
            expenses.forEach { e ->
                val v = LayoutInflater.from(this@HistoryActivity).inflate(R.layout.item_expense, binding.expenseContainer, false)
                v.findViewById<TextView>(R.id.tvItemCategory).text = e.category
                v.findViewById<TextView>(R.id.tvItemDate).text = e.date + if (e.description.isNullOrEmpty()) "" else " • ${e.description}"
                v.findViewById<TextView>(R.id.tvItemAmount).text = "R ${String.format("%.2f", e.amount)}"
                binding.expenseContainer.addView(v)
            }
        }
    }
}
