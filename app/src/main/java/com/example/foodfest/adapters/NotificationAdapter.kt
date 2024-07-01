package com.example.foodfest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfest.databinding.NotificatinItemBinding

class NotificationAdapter  (val nName : ArrayList<String>,val nImage : ArrayList<Int>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){


    inner class ViewHolder (val binding : NotificatinItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                notificationText.text=nName[position]
                notificationImage.setImageResource(nImage[position])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = NotificatinItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = nName.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }
}