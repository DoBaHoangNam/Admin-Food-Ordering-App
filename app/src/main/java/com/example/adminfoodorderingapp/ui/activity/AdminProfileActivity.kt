package com.example.adminfoodorderingapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adminfoodorderingapp.R
import com.example.adminfoodorderingapp.databinding.ActivityAdminProfileBinding
import com.example.adminfoodorderingapp.model.AdminProfile
import com.example.adminfoodorderingapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy{
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

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

        binding.btnBack.setOnClickListener {
            finish()
        }

        setUserData()
        binding.btnSaveInfo.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val address = binding.edtAdress.text.toString()
            val phone = binding.edtPhone.text.toString()
            val password = binding.edtPassword.text.toString()

            updateUserData(name,email,address,phone,password)
        }

    }

    private fun updateUserData(name: String, email: String, address: String, phone: String, password: String) {
        val userId = auth.currentUser?.uid
        if(userId != null){
            val userRef = database.getReference("user").child(userId)
            val userData = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone,
                "password" to password
            )
            userRef.setValue(userData).addOnSuccessListener {
                Toast.makeText(this, "Profile has been updated", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Profile has been updated", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setUserData(){
        val userId = auth.currentUser?.uid
        if(userId!=null){
            val userRef = database.getReference("user").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val userProfile = snapshot.getValue(AdminProfile::class.java)
                        if (userProfile != null){
                            binding.edtName.setText(userProfile.name)
                            binding.edtAdress.setText(userProfile.address)
                            binding.edtPhone.setText(userProfile.phone)
                            binding.edtEmail.setText(userProfile.email)
                            binding.edtPassword.setText(userProfile.password)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}