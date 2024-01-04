package com.example.adminfoodorderingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodorderingapp.R
import com.example.adminfoodorderingapp.model.Food
import com.example.adminfoodorderingapp.model.Order


class PendingOrderAdapter(private var items: MutableList<Order>,private val context: Context) :
    RecyclerView.Adapter<PendingOrderAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imgFood2)
        val customerName: TextView = itemView.findViewById(R.id.tvNameOfCustomer2)
        val quantities: TextView = itemView.findViewById(R.id.tvQuantity2)
        val accepBtn: Button =  itemView.findViewById(R.id.btnAccptDis)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pending_order_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.customerName.text = currentItem.customerName
        holder.image.setImageResource(currentItem.image)
        holder.quantities.text = currentItem.quantity

        var isAccepted = false
        holder.accepBtn.apply {
            if(!isAccepted){
                text = "Accept"
            }else{
                text = "Dispatch"
            }
            setOnClickListener{
                if(!isAccepted){
                    text = " Dispatch"
                    isAccepted = true
                    showToast("Order is accepted")
                }else{
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position,items.size)
                    showToast("Order is dispatched")
                }
            }
        }



    }

    private fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}

