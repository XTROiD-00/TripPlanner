package com.example.budgetx

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetx.data.AppDatabase
import com.example.budgetx.databinding.ActivityMapBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private var tripId = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); binding = ActivityMapBinding.inflate(layoutInflater); setContentView(binding.root)
        tripId = intent.getLongExtra("trip_id", -1L)
        binding.webMap.webViewClient = WebViewClient(); binding.webMap.settings.javaScriptEnabled = true; loadMap()
    }
    private fun loadMap() {
        lifecycleScope.launch {
            val trip = withContext(Dispatchers.IO) { AppDatabase.getDatabase(applicationContext).tripDao().getById(tripId) }
            val destination = trip?.destination ?: ""
            val query = URLEncoder.encode(destination, "UTF-8")
            binding.tvMapTitle.text = "Map • $destination"
            binding.webMap.loadUrl("https://www.openstreetmap.org/search?query=$query")
        }
    }
}
