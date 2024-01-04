package com.example.adminfoodorderingapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.adminfoodorderingapp.R
import com.example.adminfoodorderingapp.databinding.ActivityDashboardScreenBinding
import com.example.adminfoodorderingapp.databinding.ActivityLoginBinding
import com.example.adminfoodorderingapp.model.Order
import com.example.adminfoodorderingapp.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardScreen : AppCompatActivity() {

    private var listOfOrderItems: MutableList<OrderDetails> = mutableListOf()
    private var listOfPendingItem: MutableList<Order> = mutableListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference
    private val binding: ActivityDashboardScreenBinding by lazy{
        ActivityDashboardScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")
        countNumberPending()
        retrieveCompletedOrderDetail()


        binding.btnAllItem.setOnClickListener {
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }
        binding.btnProfile.setOnClickListener {
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogOut.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnOrderDispatch.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnAddItem.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.tvPending.setOnClickListener {
            val intent = Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun retrieveCompletedOrderDetail() {
        database =FirebaseDatabase.getInstance()
        val completeOrderReference = database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var count = 0
                var earning = 0.0
                for(orderSnapshot in snapshot.children){
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        if (it.paymentRecieved ){
                            count++

                            for (item in it.orderItems!!){
                                var quantity = item.foodQuantity?.toDouble()
                                var price = item.foodPrice?.replace("đ", "")?.toDouble()
                                if (quantity != null && price != null) {
                                    earning += quantity*price
                                }

                            }
                        }
                    }

                }

                binding.tvCompletedOrder.text = count.toString()
                var existMoney = binding.tvEarning.text.toString()
                existMoney = existMoney.replace("VND", "").trim() // Assign the modified string back to existMoney
                var sum = existMoney.toDouble() + earning
                binding.tvEarning.text = sum.toString() + "VND"

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun countNumberPending(){
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapshot in snapshot.children){
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let{
                        listOfOrderItems.add(it)
                    }
                }
                Log.d("Debug1", "List of order items: $listOfOrderItems")
                for(orderItem in listOfOrderItems){

                    Log.d("Debug2", "order items: $orderItem")

                    for (item in orderItem.orderItems!!){
                        var pendingItem = Order()
                        Log.d("Debug3", "order items: $item")
                        pendingItem.customerName = orderItem.userName
                        pendingItem.foodName = item.foodName
                        pendingItem.quantity = item.foodQuantity?.toInt() ?: 0
                        pendingItem.foodImage = item.foodImage

                        Log.d("Debug4", "order items: $pendingItem")

                        listOfPendingItem.add(pendingItem)
                    }
                }

                var count = listOfPendingItem.size
                binding.numberPending.text = count.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}