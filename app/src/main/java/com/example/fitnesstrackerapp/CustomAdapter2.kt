package com.example.fitnesstrackerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView


class CustomAdapter2(val usernames:Array<String>,val points:Array<Int>): RecyclerView.Adapter<CustomAdapter2.CustomViewHolder>(){

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindValue(txt: String, points: Int){
            itemView.findViewById<TextView>(R.id.text1).text = txt
            itemView.findViewById<TextView>(R.id.text2).text = points.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return CustomViewHolder(v)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindValue(usernames[position], points[position])
    }

    override fun getItemCount(): Int {
        return usernames.size
    }

}