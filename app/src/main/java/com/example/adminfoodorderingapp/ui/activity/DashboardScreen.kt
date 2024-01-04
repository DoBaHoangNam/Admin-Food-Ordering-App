package com.example.adminfoodorderingapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminfoodorderingapp.R
import com.example.adminfoodorderingapp.databinding.ActivityDashboardScreenBinding
import com.example.adminfoodorderingapp.databinding.ActivityLoginBinding

class DashboardScreen : AppCompatActivity() {
    private val binding: ActivityDashboardScreenBinding by lazy{
        ActivityDashboardScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAllItem.setOnClickListener {
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }
        binding.btnProfile.setOnClickListener {
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.btnCreateAdmin.setOnClickListener {
            val intent = Intent(this, CreateNewUserAdminActivity::class.java)
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
        }
        binding.btnAddItem.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.tvPending.setOnClickListener {
            val intent = Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }

    }
}