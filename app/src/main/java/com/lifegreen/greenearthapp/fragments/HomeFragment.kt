package com.lifegreen.greenearthapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lifegreen.greenearthapp.R
import com.lifegreen.greenearthapp.adapter.ProductAdapter
import com.lifegreen.greenearthapp.adapter.CategoryAdapter
import com.lifegreen.greenearthapp.databinding.FragmentHomeBinding
import com.lifegreen.greenearthapp.model.ProductModel
import com.lifegreen.greenearthapp.model.CategoryModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        getSliderImage()
        getCategories()
        getProducts()

        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        if(preference.getBoolean("isCart", false)){
          findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        }

        return binding.root
    }

    private fun getProducts() {
        val list = ArrayList<ProductModel>()
        Firebase.firestore.collection("Products").get().addOnSuccessListener {
            list.clear()
            for (doc in it.documents) {
                doc.toObject(ProductModel::class.java)?.let { data -> list.add(data) }
            }
            if(context!=null)
            binding.productRecyclerview.adapter = ProductAdapter(requireContext(), list)
        }
    }

    private fun getCategories() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories").get().addOnSuccessListener {
            list.clear()
            for (doc in it.documents) {
                doc.toObject(CategoryModel::class.java)?.let { data -> list.add(data) }
            }
            if(context!=null)
            binding.categoryRecycler.adapter = CategoryAdapter(requireContext(), list)
        }
    }

    private fun getSliderImage() {
        Firebase.firestore.collection("slider").document("item").get().addOnSuccessListener {
            if(context!=null)
            Glide.with(requireContext()).load(it.get("img")).placeholder(R.drawable.home)
                .into(binding.sliderImageView)
        }
    }

}