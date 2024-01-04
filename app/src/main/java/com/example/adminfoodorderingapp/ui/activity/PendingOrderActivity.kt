package com.example.adminfoodorderingapp.ui.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodorderingapp.adapter.PendingOrderAdapter
import com.example.adminfoodorderingapp.databinding.ActivityPendingOrderBinding
import com.example.adminfoodorderingapp.model.Order
import com.example.adminfoodorderingapp.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {

    private lateinit var binding: ActivityPendingOrderBinding
    private var listOfOrderItems: MutableList<OrderDetails> = mutableListOf()
    private var listOfPendingItem: MutableList<Order> = mutableListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPendingOrderBinding.inflate(layoutInflater)

        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")

        getOrderDetails()


        binding.btnBack.setOnClickListener {
            val intent = Intent(this, DashboardScreen::class.java)
            startActivity(intent)
            finish()
        }

    }
    override fun onBackPressed() {
        val intent = Intent(this, DashboardScreen::class.java)
        startActivity(intent)
        finish()
    }

    private fun getOrderDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItems.add(it)
                    }
                }
                Log.d("Debug1", "List of order items: $listOfOrderItems")
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun addDataToListForRecyclerView() {
        for (orderItem in listOfOrderItems) {

            Log.d("Debug2", "order items: $orderItem")

            for (item in orderItem.orderItems!!) {
                val pendingItem = Order()
                Log.d("Debug3", "order items: $item")

                orderItem.userName?.let { pendingItem.customerName = it }
                item.foodName?.let { pendingItem.foodName = it }
                item.foodQuantity.let {
                    if (it != null) {
                        pendingItem.quantity = it.toInt()
                    }
                }
                item.foodImage.let { pendingItem.foodImage = it }


                Log.d("Debug4", "order items: $pendingItem")

                listOfPendingItem.add(pendingItem)
            }
        }
        displayFoodOrder()
    }


    private fun displayFoodOrder() {
        binding.listPending.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        Log.d("Debug5", "order items: $listOfPendingItem")
        val adapter = PendingOrderAdapter(listOfPendingItem, this, this)
        binding.listPending.adapter = adapter

    }

    override fun onItemClickListener(position: Int) {

    }

    override fun onItemAcceptClickListener(position: Int) {
        if (position >= 0 && position < listOfOrderItems.size) {
            val childItemPushKey = listOfOrderItems[position].itemPushKey
            val clickItemOrderReference = childItemPushKey?.let {
                database.reference.child("OrderDetails").child(it)
            }

            clickItemOrderReference?.child("orderAccepted")?.setValue(true)
            updateOrderAcceptStatus(position)
        } else {
            Log.e("Error", "Invalid position: $position")
            // Handle the error appropriately, show a message, or do nothing.
        }
    }



    override fun onItemDispatchClickListener(position: Int) {
        val dispatchItemPushKey = listOfOrderItems[position].itemPushKey
        val dispatchItemOrderReferences = database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReferences.setValue(listOfOrderItems[position]).addOnSuccessListener {
            deleteThisItemFromOrderDetails(dispatchItemPushKey)
        }
    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderDetailsItemReference = database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this,"Order is Dispatched", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Order is not Dispatched", Toast.LENGTH_SHORT).show()
            }

    }

    private fun updateOrderAcceptStatus(position: Int) {
        val userIdOfClickItem = listOfOrderItems[position].userUid
        val pushKeyOfClickedItem = listOfOrderItems[position].itemPushKey
        val buyHistoryReference =
            database.reference.child("user").child(userIdOfClickItem!!).child("BuyHistory")
                .child(pushKeyOfClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)
    }

}