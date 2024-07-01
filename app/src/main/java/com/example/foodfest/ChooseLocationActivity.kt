package com.example.foodfest

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodfest.databinding.ActivityChooseLocationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChooseLocationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val binding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Firebase Auth ve Database referanslarını al
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val locationList = arrayOf("Kastamonu", "Samsun", "İstanbul", "Ankara")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        binding.btnNext.setOnClickListener {
            // Seçilen lokasyon bilgisini al
            val selectedLocation = autoCompleteTextView.text.toString()

            // Firebase kullanıcısını kontrol et
            val user = auth.currentUser
            if (user != null) {
                val userId = user.uid

                // Users tablosundaki address alanını güncelle
                database.reference.child("Users").child(userId).child("address").setValue(selectedLocation)
                    .addOnSuccessListener {
                        // Adres başarıyla güncellendiğinde kullanıcıyı MainActivity'e yönlendir
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("selectedLocation", selectedLocation)
                        startActivity(intent)
                    }
                    .addOnFailureListener { exception ->
                        // Hata durumunda kullanıcıya bilgi ver
                        Toast.makeText(this, "Adres güncellenemedi: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
