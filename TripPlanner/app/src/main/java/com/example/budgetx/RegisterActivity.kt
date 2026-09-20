package com.example.budgetx

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetx.data.AppDatabase
import com.example.budgetx.data.entity.User
import com.example.budgetx.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.setOnClickListener { register() }
    }

    private fun register() {
        val name = binding.etRegUsername.text.toString().trim()
        val email = binding.etRegEmail.text.toString().trim()
        val password = binding.etRegPassword.text.toString()
        val confirm = binding.etRegConfirmPassword.text.toString()
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show(); return
        }
        if (password != confirm) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show(); return
        }
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val existing = withContext(Dispatchers.IO) { db.userDao().findByEmail(email) }
            if (existing != null) {
                Toast.makeText(this@RegisterActivity, "Email already registered", Toast.LENGTH_SHORT).show(); return@launch
            }
            withContext(Dispatchers.IO) { db.userDao().insert(User(name = name, email = email, password = password)) }
            Toast.makeText(this@RegisterActivity, "Account created", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
