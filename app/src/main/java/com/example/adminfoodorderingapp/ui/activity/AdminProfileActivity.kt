package com.example.adminfoodorderingapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminfoodorderingapp.R
import com.example.adminfoodorderingapp.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy{
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.edtName.isEnabled = false
        binding.edtAdress.isEnabled = false
        binding.edtPhone.isEnabled = false
        binding.edtEmail.isEnabled = false
        binding.edtPassword.isEnabled = false

        var isEnable = false
        binding.tcClickToEdit.setOnClickListener {
            isEnable = ! isEnable
            binding.edtName.isEnabled = isEnable
            binding.edtAdress.isEnabled = isEnable
            binding.edtPhone.isEnabled = isEnable
            binding.edtEmail.isEnabled = isEnable
            binding.edtPassword.isEnabled = isEnable
            if(isEnable){
                binding.edtName.requestFocus()
            }

        }

    }
}