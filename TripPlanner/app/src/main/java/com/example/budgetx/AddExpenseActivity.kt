package com.example.budgetx

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.budgetx.data.AppDatabase
import com.example.budgetx.data.entity.Expense
import com.example.budgetx.databinding.ActivityAddExpenseBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddExpenseBinding
    private var currentPhotoPath: String? = null
    private var photoUri: Uri? = null
    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success -> if(success){binding.ivPhotoPreview.visibility=View.VISIBLE;binding.ivPhotoPreview.setImageURI(photoUri)}else{currentPhotoPath=null;photoUri=null} }
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); binding=ActivityAddExpenseBinding.inflate(layoutInflater);setContentView(binding.root); setupCategory(); binding.etDate.setOnClickListener{pickDate()};binding.btnAttachPhoto.setOnClickListener{setupCamera()};binding.btnSaveExpense.setOnClickListener{saveExpenseToDatabase()} }
    private fun setupCategory(){val items=arrayOf("Accommodation","Food","Transport","Activities","Shopping","Other");binding.etCategory.setAdapter(ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,items));binding.etCategory.setOnClickListener{binding.etCategory.showDropDown()}}
    private fun pickDate(){val c=Calendar.getInstance();DatePickerDialog(this,{_,y,m,d->c.set(y,m,d);binding.etDate.setText(SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(c.time))},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show()}
    private fun setupCamera(){val dir=getExternalFilesDir(Environment.DIRECTORY_PICTURES)?:return;val f=File.createTempFile("TRIP_${SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(Date())}_",".jpg",dir);currentPhotoPath=f.absolutePath;photoUri=FileProvider.getUriForFile(this,"${packageName}.fileprovider",f);takePhotoLauncher.launch(photoUri)}
    private fun saveExpenseToDatabase(){val tripId=intent.getLongExtra("trip_id",-1L);val amountText=binding.etAmount.text.toString().trim();val category=binding.etCategory.text.toString().trim();val date=binding.etDate.text.toString().trim();if(tripId<=0||amountText.isEmpty()||category.isEmpty()||date.isEmpty()){Toast.makeText(this,"Please fill in amount, category and date",Toast.LENGTH_SHORT).show();return};val amount=amountText.toDoubleOrNull();if(amount==null||amount<=0){binding.etAmount.error="Enter a valid amount";return};val e=Expense(tripId=tripId,amount=amount,category=category,description=binding.etDescription.text.toString().trim().ifEmpty{null},date=date,photoPath=currentPhotoPath);binding.btnSaveExpense.isEnabled=false;lifecycleScope.launch{try{withContext(Dispatchers.IO){AppDatabase.getDatabase(applicationContext).expenseDao().insert(e)};Toast.makeText(this@AddExpenseActivity,"Expense saved",Toast.LENGTH_SHORT).show();finish()}catch(_:Exception){binding.btnSaveExpense.isEnabled=true;Toast.makeText(this@AddExpenseActivity,"Could not save expense",Toast.LENGTH_SHORT).show()}}}
}
