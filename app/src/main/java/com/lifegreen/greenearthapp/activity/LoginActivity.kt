package com.lifegreen.greenearthapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lifegreen.greenearthapp.databinding.ActivityLoginBinding
import com.lifegreen.greenearthapp.model.UserDetails
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var dialog: AlertDialog
    private var userDetails: ArrayList<UserDetails>? = ArrayList()
    private var userList: ArrayList<String>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        binding.loginPhoneNumberEditText.setText(intent.getStringExtra("phoneNum"))
        binding.loginSignInBtn.setOnClickListener {
            getRegisteredUserList()
            if (binding.loginPhoneNumberEditText.text.toString().isEmpty()
                || binding.loginPhoneNumberEditText.text?.length!! < 10
            ) {
                binding.loginPhoneNumberEditText.error = "Provide Valid  Phone Number"
            } else if(userList?.contains(binding.loginPhoneNumberEditText.text.toString()) != true) {
                Toast.makeText(this,"Please Sign Up", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
            else{
                sendOtp(binding.loginPhoneNumberEditText.text.toString())
            }
        }
    }

    private fun getRegisteredUserList() {

       Firebase.firestore.collection("users").get().addOnSuccessListener {
           for (doc in it.documents){
               doc.toObject(UserDetails::class.java)?.let { it1 -> userDetails?.add(it1) }
           }
       }
        for (user in userDetails!!){
            user.phoneNumber?.let { userList?.add(it.toString()) }
        }
    }


    fun sendOtp(phoneNumber: String) {
        dialog = AlertDialog.Builder(this)
            .setTitle("Loading")
            .setMessage("Please Wait...")
            .setCancelable(false).create()


        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber("+91$phoneNumber")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@LoginActivity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        dialog.show()

    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(e: FirebaseException) {


            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Toast.makeText(this@LoginActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                dialog.dismiss()
            } else {
                dialog.dismiss()
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {

            dialog.dismiss()

                 val intent = Intent(this@LoginActivity, OTPActivity::class.java)
                 intent.putExtra("verificationId", verificationId)
                 intent.putExtra("phoneNumber", binding.loginPhoneNumberEditText.text.toString())
                 startActivity(intent)

        }
    }
}
