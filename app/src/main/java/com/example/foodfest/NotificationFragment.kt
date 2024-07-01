package com.example.foodfest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfest.adapters.NotificationAdapter
import com.example.foodfest.databinding.FragmentNotificationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NotificationFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentNotificationBinding.inflate(inflater, container, false)
        binding.imageView6.setOnClickListener {
            dismiss()
        }
        val names= listOf("Siparişiniz Başarıyla İptal Edildi","Siparişiniz Teslim İçin Yola Çıktı","Tebrikler Siparişiniz Verildi")
        val images = listOf(R.drawable.sad_emoji,R.drawable.delivery_green,R.drawable.success_illu)
        val adapter = NotificationAdapter(ArrayList(names), ArrayList(images))
        binding.NRv.layoutManager=LinearLayoutManager(requireContext())
        binding.NRv.adapter=adapter
       return binding.root
    }

    companion object {

    }
}