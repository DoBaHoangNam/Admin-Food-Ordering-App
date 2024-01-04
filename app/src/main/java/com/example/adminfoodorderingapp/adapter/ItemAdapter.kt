package com.example.adminfoodorderingapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodorderingapp.R
import com.example.adminfoodorderingapp.model.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class ItemAdapter(
    private val context: Context,
    private val items: ArrayList<Item>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    val itemQuantities = IntArray(items.size) { 1 }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imgFood)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val foodName: TextView = itemView.findViewById(R.id.tvNameOfFood)
        val quantities: TextView = itemView.findViewById(R.id.quantitiesTv)
        val minusBtn: ImageView = itemView.findViewById(R.id.minusBtn)
        val plusBtn: ImageView = itemView.findViewById(R.id.plusBtn)
        val deleteBtn: ImageView = itemView.findViewById(R.id.trashBtn)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.all_item_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        val uriString = currentItem.image
        val uri = Uri.parse(uriString)

        holder.foodName.text = currentItem.foodName
        holder.price.text = currentItem.price
        Glide.with(context).load(uri).into(holder.image)
        holder.quantities.text = currentItem.quantity.toString()



        holder.minusBtn.setOnClickListener {
            updateQuantity(currentItem, -1)
        }
        holder.plusBtn.setOnClickListener {
            updateQuantity(currentItem, 1)
        }
        holder.deleteBtn.setOnClickListener {
            val databaseReference = FirebaseDatabase.getInstance().getReference("menu")
            val currentItem = items[position]

            if (currentItem.itemKey != null) {
                databaseReference.child(currentItem.itemKey).removeValue()
                    .addOnSuccessListener {
                        items.remove(currentItem)
                        notifyDataSetChanged()
                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                    }

                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("menu_images${currentItem.itemKey}.jpg")
                imageRef.delete()
            }


        }
    }

    private fun updateQuantity(item: Item, change: Int) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("menu")

        if (item.itemKey != null) {
            // get current quantity
            val currentQuantity = item.quantity

            // Update quantity
            val newQuantity = currentQuantity + change

            if (newQuantity < 1 ){
                databaseReference.child(item.itemKey).removeValue()
                    .addOnSuccessListener {
                        items.remove(item)
                        notifyDataSetChanged()
                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                    }

                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("menu_images${item.itemKey}.jpg")
                imageRef.delete()
            }else{
                // save new info for item
                val newItem = Item(
                    itemKey = item.itemKey,
                    foodName = item.foodName,
                    price = item.price,
                    description = item.description,
                    ingredient = item.ingredient,
                    image = item.image,
                    quantity = newQuantity
                )

                // Update value Firebase Realtime Database
                databaseReference.child(item.itemKey).setValue(newItem)
                    .addOnSuccessListener {
                        item.quantity = newQuantity
                        notifyDataSetChanged()
                        Toast.makeText(context, "Quantity updated successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        // Update fail
                        Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }


}

