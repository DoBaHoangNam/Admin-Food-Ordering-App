package com.example.adminfoodorderingapp.ui.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodorderingapp.R
import com.example.adminfoodorderingapp.adapter.ItemAdapter
import com.example.adminfoodorderingapp.adapter.PendingOrderAdapter
import com.example.adminfoodorderingapp.databinding.ActivityAllItemBinding
import com.example.adminfoodorderingapp.databinding.ActivityDashboardScreenBinding
import com.example.adminfoodorderingapp.databinding.ActivityPendingOrderBinding
import com.example.adminfoodorderingapp.model.Food
import com.example.adminfoodorderingapp.model.Order

class PendingOrderActivity : AppCompatActivity() {
    private val binding: ActivityPendingOrderBinding by lazy{
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        displayFoodOrder()
        binding.btnBack.setOnClickListener {
            finish()
        }
    }


    private fun displayFoodOrder() {
        binding.listPending.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        val adapter = PendingOrderAdapter(getListPendingOrder(), this )
        binding.listPending.adapter = adapter


    }

    private fun getListPendingOrder(): MutableList<Order> {
        val list = mutableListOf<Order>()
        list.add(Order("Burger", "10", R.drawable.menu1))
        list.add(Order("Sandwich", "16", R.drawable.menu2))
        list.add(Order("Momo", "13", R.drawable.menu3))
        list.add(Order("Hamburger", "18", R.drawable.menu4))
        list.add(Order("Burger", "11", R.drawable.menu5 ))
        list.add(Order("Sandwich", "5", R.drawable.menu6))

        return list
    }
}