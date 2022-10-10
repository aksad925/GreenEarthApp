package com.lifegreen.greenearthapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.lifegreen.greenearthapp.activity.AddressActivity
import com.lifegreen.greenearthapp.activity.CategoryActivity
import com.lifegreen.greenearthapp.adapter.CartAdapter
import com.lifegreen.greenearthapp.databinding.FragmentCartBinding
import com.lifegreen.greenearthapp.model.UserModel
import com.lifegreen.greenearthapp.roomdb.AppDatabase
import com.lifegreen.greenearthapp.roomdb.ProductModel


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var userIdList: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)

        val preference =
            requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", false)
        editor.apply()

        val dao = AppDatabase.getInstance(requireContext())?.productDao()
        userIdList = ArrayList()
        dao?.getAllProducts()?.observe(requireActivity()) {
            binding.fragmentCartRecyclerview.adapter = CartAdapter(requireContext(), it)
            userIdList.clear()

            for (data in it) {
                userIdList.add(data.productId)
            }
            totalCost(it)
        }


        return binding.root
    }

    private fun totalCost(productList: List<ProductModel>?) {
        var total = 0.0
        if (productList != null) {
            for (data in productList) {
                total += data.productSp?.toDouble()!!
            }
        }
        binding.totalCartItems.text = "Total Item in Cart is ${productList?.size}"
        binding.totalCartPrice.text = "Total Cost: ${total}"
        binding.fragmentCartCheckoutBtn.setOnClickListener {
            val intent = Intent(requireContext(), AddressActivity::class.java)
            intent.putExtra("totalCost", total.toString())
            intent.putStringArrayListExtra("productIds", userIdList)
            startActivity(intent)
        }
    }

}