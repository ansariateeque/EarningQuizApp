package com.example.earningapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.earningapp.databinding.FragmentSpinBinding
import com.example.earningapp.databinding.HistorylayoutBinding
import com.example.earningapp.models.HistoryModel

class HistoryAdapter(val arrayList: ArrayList<HistoryModel>):
    RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      return MyViewHolder(HistorylayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var historyModel=arrayList.get(position)
        holder.binding.daytime.setText(historyModel.timeanddate)
        holder.binding.rupees.setText(historyModel.rupees)
        holder.binding.withdrawalOrEarned.setText(historyModel.withdrawalOrEarned)
    }

    class MyViewHolder(var binding: HistorylayoutBinding): RecyclerView.ViewHolder(binding.root) {

    }
}