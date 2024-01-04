package com.example.adminfoodorderingapp.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodorderingapp.R
import com.example.adminfoodorderingapp.databinding.DeliveryItemBinding


class DeliveryAdapter(
    private var customername: MutableList<String>,
    private val moneyStatus: MutableList<Boolean>
) :
    RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int = customername.size

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)

    }

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.apply {
                tvNameOfCustomer.text = customername[position]
                if(moneyStatus[position] == true){
                    tvStatus.text = "Recieved"
                }else{
                    tvStatus.text = "Not Recieved"
                }
                val colorMap = mapOf(
                    true to Color.GREEN, false to Color.RED
                )
                tvStatus.setTextColor(colorMap[moneyStatus[position]]?:Color.BLACK)
                imgStatus.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatus[position]]?:Color.BLACK)
            }
        }
    }


}

