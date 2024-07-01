package com.example.foodfest

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foodfest.databinding.ActivityDetailsBinding
import com.example.foodfest.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var foodImages : String
    private lateinit var foodIngredients : String
    private lateinit var foodDescriptions : String
    private lateinit var foodPrices : String
    private lateinit var foodNames:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

         foodNames = intent.getStringExtra("MenuItem").toString()
        foodImages = intent.getStringExtra("MenuImage").toString()
       foodPrices = intent.getStringExtra("MenuPrice").toString()
        foodDescriptions = intent.getStringExtra("MenuDescription").toString()
       foodIngredients = intent.getStringExtra("MenuIngredients").toString()

        binding.ingredientsTextView.text = foodIngredients
        binding.descriptionTextView.text = foodDescriptions
        Glide.with(this).load(Uri.parse(foodImages)).into(binding.imageView9)
        binding.detailsFoodName.text = foodNames




        binding.detailsBackBtn.setOnClickListener {
            finish()
        }
        binding.detailsAddCartBtn.setOnClickListener {
            addItemToCart()
        }

    }

    private fun addItemToCart() {


        val database = FirebaseDatabase.getInstance().reference
        val userUid = auth.currentUser?.uid ?: ""

        // Create a cartItem object
        val cartItems = CartItems(
            foodNames,
            foodPrices,
            foodDescriptions,
            foodImages,
            1
        )

        // Save data to cart item to firebase database
        database.child("Users").child(userUid).child("Cart Items").push().setValue(cartItems)
            .addOnSuccessListener {

                Toast.makeText(this, "Ürün sepete başarıyla eklendi", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
            Toast.makeText(this, "Ürün eklenemedi", Toast.LENGTH_SHORT).show()
        }
    }
}