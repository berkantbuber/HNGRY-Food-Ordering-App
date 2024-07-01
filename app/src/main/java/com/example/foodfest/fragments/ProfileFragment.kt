package com.example.foodfest.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.foodfest.LogInActivity
import com.example.foodfest.databinding.FragmentProfileBinding
import com.example.foodfest.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentProfileBinding
    private var selectedLocation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedLocation = it.getString("selectedLocation")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()

        binding.apply {
            profileName.isEnabled = false
            profileEmail.isEnabled = false
            profileAddress.isEnabled = false
            profileNumber.isEnabled = false
            save.visibility = View.INVISIBLE

            editBtn.setOnClickListener {
                profileName.isEnabled = true
                profileEmail.isEnabled = true
                profileAddress.isEnabled = true
                profileNumber.isEnabled = true
                save.visibility = View.VISIBLE
            }

            save.setOnClickListener {
                val name = profileName.text.toString()
                val email = profileEmail.text.toString()
                val address = profileAddress.text.toString()
                val number = profileNumber.text.toString()
                updateUserData(name, email, address, number)
            }

            logoutBtn.setOnClickListener {
                logout()
            }
        }

        setUserData()

        // Seçilen lokasyon bilgisini adres alanına ekleyelim
        selectedLocation?.let {
            binding.profileAddress.setText(it)
        }

        return binding.root
    }

    private fun updateUserData(name: String, email: String, address: String, number: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.child("Users").child(userId)
            val userData: HashMap<String, String> = hashMapOf(
                "name" to name,
                "email" to email,
                "address" to address,
                "number" to number
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profil bilgileri başarılı şekilde güncellendi.", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Profil bilgileri güncellenemedi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.child("Users").child(userId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        Log.d("profile", "onDataChange: $userProfile")
                        if (userProfile != null) {
                            binding.profileName.setText(userProfile.name)
                            binding.profileAddress.setText(userProfile.address)
                            binding.profileEmail.setText(userProfile.email)
                            binding.profileNumber.setText(userProfile.number)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    private fun logout() {
        auth.signOut()
        val intent = Intent(requireContext(), LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
