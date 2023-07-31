package com.example.earningapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.earningapp.Quiz
import com.example.earningapp.R
import com.example.earningapp.models.CategoryModel

class CategoryAdapter(var list: ArrayList<CategoryModel>, var context: Context) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: View =
            LayoutInflater.from(context).inflate(R.layout.category_itmes, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var model:CategoryModel=list.get(position)
        holder.itemImage.setImageResource(model.itemImage)
        holder.itemText.setText(model.itemName)

        holder.itemView.setOnClickListener{
            var intent=Intent(context, Quiz()::class.java)
            intent.putExtra("key_image",model.itemImage)
            intent.putExtra("subject",model.itemName)
            context.startActivity(intent)
        }

    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.itemimage)
        var itemText: TextView = itemView.findViewById(R.id.itemname)
    }

}