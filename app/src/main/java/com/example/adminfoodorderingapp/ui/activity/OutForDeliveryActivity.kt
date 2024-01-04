package com.example.adminfoodorderingapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodorderingapp.R
import com.example.adminfoodorderingapp.adapter.DeliveryAdapter
import com.example.adminfoodorderingapp.adapter.ItemAdapter
import com.example.adminfoodorderingapp.databinding.ActivityOutForDeliveryBinding
import com.example.adminfoodorderingapp.model.Delivery
import com.example.adminfoodorderingapp.model.Food
import com.example.adminfoodorderingapp.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding :ActivityOutForDeliveryBinding by lazy {
       ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference
    private var listOfCompleteOrderList: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        displayDelivery()
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, DashboardScreen::class.java)
            startActivity(intent)
            finish()
        }

        retrieveCompletedOrderDetail()

    }

    override fun onBackPressed() {
        val intent = Intent(this, DashboardScreen::class.java)
        startActivity(intent)
        finish()
    }

    private fun retrieveCompletedOrderDetail() {
        database =FirebaseDatabase.getInstance()
        val completeOrderReference = database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrderList.clear()
                for(orderSnapshot in snapshot.children){
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }
                }
                listOfCompleteOrderList.reverse()

                setDataIntoRecycleView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setDataIntoRecycleView() {
        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()
        for(order in listOfCompleteOrderList){
            order.userName?.let{
                customerName.add(it)
            }
            moneyStatus.add(order.paymentRecieved)
        }

        binding.listDelivery.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        val adapter = DeliveryAdapter(customerName, moneyStatus)
        binding.listDelivery.adapter = adapter
    }

    private fun displayDelivery() {

    }


}