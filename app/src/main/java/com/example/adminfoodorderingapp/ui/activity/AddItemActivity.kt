package com.example.adminfoodorderingapp.ui.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminfoodorderingapp.R
import com.example.adminfoodorderingapp.databinding.ActivityAddItemBinding
import com.example.adminfoodorderingapp.model.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null
    //Firebase

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddBtn.setOnClickListener {
            foodName = binding.edtFoodName.text.toString().trim()
            foodPrice = binding.edtFoodPrice.text.toString().trim() + "đ"
            foodDescription = binding.tvDetail.text.toString().trim()
            foodIngredient = binding.tvIngredients.text.toString().trim()

            if (!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngredient.isBlank())) {
                uploadData()
                Toast.makeText(this, "Item Add Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill all the details", Toast.LENGTH_SHORT).show()

            }

        }

        binding.btnAddImg.setOnClickListener {
            pickImage.launch("image/*")
        }

    }

    private fun uploadData() {
        val menuRef = database.getReference("menu")
        val newItemKey = menuRef.push().key

        if (foodImageUri != null){
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_images${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl->
                    val newItem = Item(
                        itemKey = newItemKey,
                        foodName = foodName,
                        price = foodPrice,
                        description = foodDescription,
                        ingredient = foodIngredient,
                        image = downloadUrl.toString(),
                        quantity = 1,

                    )
                    newItemKey?.let{
                        key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this,"Data uploaded successfully", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this,"Data uploaded failed", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                    .addOnFailureListener {
                        Toast.makeText(this,"Image uploaded failed", Toast.LENGTH_SHORT).show()
                    }
            }
        }else{
            Toast.makeText(this,"Please select uploaded failed", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImg.setImageURI(uri)
            foodImageUri = uri
        }
    }
}