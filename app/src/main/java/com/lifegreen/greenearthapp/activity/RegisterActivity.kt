package com.lifegreen.greenearthapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lifegreen.greenearthapp.R
import com.lifegreen.greenearthapp.databinding.ActivityRegisterBinding
import com.lifegreen.greenearthapp.model.UserModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog = AlertDialog.Builder(this)
            .setTitle("Loading")
            .setMessage("Please Wait...")
            .setCancelable(false).create()

        val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("number", binding.registerPhoneNumberEditText.text.toString())
        editor.putString("name", binding.registerNameEditText.text.toString())
        editor.apply()

        binding.registerLoginBtn.setOnClickListener {
            openLogin()
        }

        binding.registerSignUpBtn.setOnClickListener {
            validateUser()
        }
    }

    private fun validateUser() {
        if (binding.registerNameEditText.text.toString().isEmpty()) {
            binding.registerNameEditText.error = "Please Enter User Name"
        } else if (binding.registerPhoneNumberEditText.text.toString().isEmpty() ||
            binding.registerPhoneNumberEditText.text?.length!! < 10
        ) {
            binding.registerPhoneNumberEditText.error = "Enter Valid Phone Number"
        } else {
            dialog.show()
            storeData()
        }
    }

    private fun storeData() {
        val data = UserModel(
            userName = binding.registerNameEditText.text.toString(),
            userPhoneNumber = binding.registerPhoneNumberEditText.text.toString()
        )
        Firebase.firestore.collection("users")
            .document(binding.registerPhoneNumberEditText.text.toString())
            .set(data).addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(this, "User Successfully Registered", Toast.LENGTH_SHORT).show()
                openLogin()
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }

    }

    private fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("phoneNum", binding.registerPhoneNumberEditText.text.toString())
        startActivity(intent)
        finish()
    }
}