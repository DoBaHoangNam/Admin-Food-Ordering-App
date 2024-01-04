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

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding :ActivityOutForDeliveryBinding by lazy {
       ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        displayDelivery()
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, DashboardScreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun displayDelivery() {
        binding.listDelivery.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        val adapter = DeliveryAdapter(getListDelivery())
        binding.listDelivery.adapter = adapter
    }

    private fun getListDelivery(): MutableList<Delivery> {
        val list = mutableListOf<Delivery>()
        list.add(Delivery("Burger", "Not Received"))
        list.add(Delivery("Sandwich", "Received"))
        list.add(Delivery("Burger", "Not Received"))
        list.add(Delivery("Sandwich", "Pending"))
        list.add(Delivery("Burger", "Not Received"))
        list.add(Delivery("Sandwich", "Received"))
        return list
    }
}