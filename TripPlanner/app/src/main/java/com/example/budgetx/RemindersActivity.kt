package com.example.budgetx

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetx.data.AppDatabase
import com.example.budgetx.data.entity.Reminder
import com.example.budgetx.databinding.ActivityRemindersBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RemindersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemindersBinding
    private var tripId = -1L
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); binding = ActivityRemindersBinding.inflate(layoutInflater); setContentView(binding.root); tripId = intent.getLongExtra("trip_id", -1L)
        binding.etReminderDate.setOnClickListener { pickDate() }; binding.etReminderTime.setOnClickListener { pickTime() }; binding.btnAddReminder.setOnClickListener { addReminder() }; loadReminders()
    }
    private fun pickDate() { val c=Calendar.getInstance(); DatePickerDialog(this,{_,y,m,d->c.set(y,m,d);binding.etReminderDate.setText(dateFormat.format(c.time))},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show() }
    private fun pickTime() { val c=Calendar.getInstance(); TimePickerDialog(this,{_,h,m->binding.etReminderTime.setText(String.format(Locale.getDefault(),"%02d:%02d",h,m))},c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show() }
    private fun addReminder() {
        val title=binding.etReminderTitle.text.toString().trim(); val date=binding.etReminderDate.text.toString().trim(); val time=binding.etReminderTime.text.toString().trim()
        if(tripId<=0||title.isEmpty()||date.isEmpty()||time.isEmpty()){Toast.makeText(this,"Complete the reminder fields",Toast.LENGTH_SHORT).show();return}
        lifecycleScope.launch {
            val id=withContext(Dispatchers.IO){AppDatabase.getDatabase(applicationContext).reminderDao().insert(Reminder(tripId=tripId,title=title,reminderDateTime="$date $time"))}
            scheduleNotification(id,title,date,time); binding.etReminderTitle.text.clear(); loadReminders(); Toast.makeText(this@RemindersActivity,"Reminder saved",Toast.LENGTH_SHORT).show()
        }
    }
    private fun scheduleNotification(id:Long,title:String,date:String,time:String){
        try { val sdf=SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault()); val millis=sdf.parse("$date $time")?.time?:return; val intent=Intent(this,ReminderReceiver::class.java).putExtra("title",title); val pi=PendingIntent.getBroadcast(this,id.toInt(),intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE); getSystemService(AlarmManager::class.java).set(AlarmManager.RTC_WAKEUP,millis,pi) } catch (_:Exception) {}
    }
    private fun loadReminders(){ lifecycleScope.launch { val list=withContext(Dispatchers.IO){AppDatabase.getDatabase(applicationContext).reminderDao().getByTrip(tripId)}; binding.reminderContainer.removeAllViews(); list.forEach{r->val v=LayoutInflater.from(this@RemindersActivity).inflate(R.layout.item_reminder,binding.reminderContainer,false);v.findViewById<TextView>(R.id.tvReminderTitle).text=r.title;v.findViewById<TextView>(R.id.tvReminderTime).text=r.reminderDateTime;binding.reminderContainer.addView(v)} } }
}
