package com.lifegreen.greenearthapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lifegreen.greenearthapp.R
import com.lifegreen.greenearthapp.databinding.ActivityAddressBinding
import com.lifegreen.greenearthapp.databinding.ActivityCheckoutBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityCheckoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val checkout = Checkout()
        checkout.setKeyID("rzp_test_M6BrYMkwux0hCK");
       val price = intent.getStringExtra("totalCost")
        try {
            val options = JSONObject()
            options.put("name", "GreenEarthApp")
            options.put("description", "Make your Life Freen")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#FFBB86FC");
            options.put("currency", "INR");
            options.put("amount", (price?.toInt())?.times(100) ?:0)//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email", "abhishekuae563@gmail.com")
            prefill.put("contact", "7085918328")

            options.put("prefill", prefill)
            checkout.open(this@CheckoutActivity, options)
        } catch (e: Exception) {
            Toast.makeText(
                this@CheckoutActivity,
                "Error in payment: " + e.message,
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this@CheckoutActivity, "Payment Success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this@CheckoutActivity, "Payment Failed", Toast.LENGTH_SHORT).show()
    }
}