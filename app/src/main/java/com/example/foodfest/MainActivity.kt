package com.example.foodfest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodfest.databinding.ActivityMainBinding
import com.example.foodfest.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val selectedLocation = intent.getStringExtra("selectedLocation")

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navController=findNavController(R.id.fragmentContainerView)

        bottomNav.setupWithNavController(navController)

        binding.imageButton.setOnClickListener {
            val bottomSheetDialog = NotificationFragment()
            bottomSheetDialog.show(supportFragmentManager,"Test")
        }

        val profileFragment = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString("selectedLocation", selectedLocation)
            }

        }
    }
}