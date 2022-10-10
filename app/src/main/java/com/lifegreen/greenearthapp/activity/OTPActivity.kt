package com.lifegreen.greenearthapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.lifegreen.greenearthapp.MainActivity
import com.lifegreen.greenearthapp.R
import com.lifegreen.greenearthapp.databinding.ActivityLoginBinding
import com.lifegreen.greenearthapp.databinding.ActivityOtpactivityBinding

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var dialog:AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this)
            .setTitle("Loading")
            .setMessage("Please Wait...")
            .setCancelable(false).create()


        binding.verifyOtpBtn.setOnClickListener {
            if (binding.verifyOtpEditText.text.toString().isEmpty()) {
                Toast.makeText(this@OTPActivity, "Please Enter OTP", Toast.LENGTH_SHORT).show()
            } else {
                dialog.show()
                verifyUser(binding.verifyOtpEditText.text.toString())
            }
        }

    }

    private fun verifyUser(otpText: String) {
        dialog.show()
        val verificationId = intent.getStringExtra("verificationId")
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otpText)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.putString("number", intent.getStringExtra("number"))
                    editor.apply()

                    dialog.dismiss()
                    startActivity(Intent(this@OTPActivity, MainActivity::class.java))
                    finish()
                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        dialog.dismiss()
                        Toast.makeText(this@OTPActivity, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }
    }
}