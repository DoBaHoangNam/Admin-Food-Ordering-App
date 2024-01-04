package com.example.adminfoodorderingapp.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodorderingapp.R
import com.example.adminfoodorderingapp.model.Delivery
import com.example.adminfoodorderingapp.model.Food


class DeliveryAdapter(private var items: MutableList<Delivery>) :
    RecyclerView.Adapter<DeliveryAdapter.ViewHolder>() {
  
    val itemQuantities = IntArray(items.size){1}
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val customerName: TextView = itemView.findViewById(R.id.tvNameOfCustomer)
        val status: TextView = itemView.findViewById(R.id.tvStatus)
        val imgStatus: CardView = itemView.findViewById(R.id.imgStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.delivery_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.customerName.text = currentItem.customerName
        holder.status.text = currentItem.status
        val colorMap = mapOf(
            "Received" to Color.GREEN, "Not Received" to Color.RED, "Pending" to Color.GRAY
        )
        holder.status.setTextColor(colorMap[currentItem.status]?:Color.BLACK)
        holder.imgStatus.backgroundTintList = ColorStateList.valueOf(colorMap[currentItem.status]?:Color.BLACK)


    }


}

