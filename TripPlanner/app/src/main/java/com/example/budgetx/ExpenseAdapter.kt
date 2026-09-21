package com.example.budgetx

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetx.data.entity.Expense
import com.example.budgetx.databinding.ItemExpenseBinding
import java.io.File
// This code 
class ExpenseAdapter(private var expenses: List<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {
    class ExpenseViewHolder(val binding: ItemExpenseBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int)=ExpenseViewHolder(ItemExpenseBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    override fun onBindViewHolder(holder:ExpenseViewHolder,position:Int){val e=expenses[position];holder.binding.tvItemCategory.text=e.category;holder.binding.tvItemDate.text=e.date;holder.binding.tvItemAmount.text="R ${String.format("%.2f",e.amount)}";if(e.photoPath!=null){holder.binding.ivItemPhoto.visibility=View.VISIBLE;holder.binding.ivItemPhoto.setImageURI(Uri.fromFile(File(e.photoPath)))}else holder.binding.ivItemPhoto.visibility=View.GONE}
    override fun getItemCount()=expenses.size
    fun updateData(newExpenses:List<Expense>){expenses=newExpenses;notifyDataSetChanged()}
}
