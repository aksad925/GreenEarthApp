package com.lifegreen.greenearthapp.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lifegreen.greenearthapp.databinding.ActivityAddressBinding

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    private lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = this.getSharedPreferences("user", MODE_PRIVATE)
        loadUserInfo()
        binding.addressProceedToCheckout.setOnClickListener {
            validateData(
                binding.addressVillage.text.toString(),
                binding.addressCity.text.toString(),
                binding.addressState.text.toString(),
                binding.addressPinCode.text.toString()
            )
        }
    }

    private fun validateData(village: String, city: String, state: String, pinCode: String) {
        if (TextUtils.isEmpty(village)) {
            binding.addressVillage.error = "Enter Village"
        } else if (TextUtils.isEmpty(city)) {
            binding.addressCity.error = "Enter City"
        } else if (TextUtils.isEmpty(state)) {
            binding.addressState.error = "Enter state"
        } else if (TextUtils.isEmpty(pinCode)) {
            binding.addressPinCode.error = "Enter Pin Code"
        } else {
            storeData(village, city, state, pinCode)
        }
    }

    private fun storeData(village: String, city: String, state: String, pinCode: String) {
        val data = hashMapOf<String, Any>()
        data["village"] = village
        data["city"] = city
        data["state"] = state
        data["pinCode"] = pinCode

        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "7085918328")!!).update(data)
            .addOnSuccessListener {
                val intent = Intent(this@AddressActivity, CheckoutActivity::class.java)
                intent.putStringArrayListExtra("productIds", intent.getStringArrayListExtra("productIds"))
                intent.putExtra("totalCost", intent.getStringExtra("totalCost"))
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this@AddressActivity, "Failed to store data", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun loadUserInfo() {
        val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
        preferences.getString("number", "7085918328")?.let { pref ->
            Firebase.firestore.collection("users").document(pref).get().addOnSuccessListener {
                binding.addressUserName.setText(it.getString("userName"))
                binding.addressNumber.setText(it.getString("userPhoneNumber"))
                binding.addressVillage.setText(it.getString("village"))
                binding.addressCity.setText(it.getString("city"))
                binding.addressPinCode.setText(it.getString("pinCode"))
                binding.addressState.setText(it.getString("state"))
            }.addOnFailureListener {
                Toast.makeText(this@AddressActivity, "failed to Load user data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}