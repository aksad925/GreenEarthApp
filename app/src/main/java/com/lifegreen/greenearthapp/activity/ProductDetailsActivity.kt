package com.lifegreen.greenearthapp.activity

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lifegreen.greenearthapp.MainActivity
import com.lifegreen.greenearthapp.R
import com.lifegreen.greenearthapp.databinding.ActivityProductDetailsBinding
import com.lifegreen.greenearthapp.roomdb.AppDatabase
import com.lifegreen.greenearthapp.roomdb.ProductDao
import com.lifegreen.greenearthapp.roomdb.ProductModel
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        getProductDetails(intent.getStringExtra("id"))
        setContentView(binding.root)

    }

    private fun getProductDetails(productId: String?) {
        Firebase.firestore.collection("Products").document(productId!!)
            .get().addOnSuccessListener {

                val list = it.get("productImages") as ArrayList<String>
                val slideModelList = ArrayList<SlideModel>()
                for (data in list) {
                    slideModelList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
                }
                cartAction(productId,
                    it.getString("productName"),
                    it.getString("productMrp"),
                it.getString("productSp"),
                it.getString("productCoverImage"))
                binding.imageSlider.setImageList(slideModelList)
                binding.productDetailMrpTextview.text = "₹" + it.getString("productMrp")
                binding.productDetailMrpTextview.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                binding.productDetailSpTextview.text = "₹" + it.getString("productSp")
                binding.productDetailName.text = it.getString("productName")
                binding.productDetailDescTextview.text = it.getString("productDescription")
                val per = ((it.getString("productMrp")?.toDouble()
                    ?.minus(it.getString("productSp")!!.toDouble()))?.div(
                        it.getString("productMrp")?.toDouble()!!
                    ))?.times(
                        100
                    );
                binding.productDetailOffTextview.text = getString(R.string.flat_off_text, per)

            }.addOnFailureListener {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cartAction(productId: String, productName: String?, productMrp: String?, productSp: String?, productCoverImage: String?) {
     val productdao = AppDatabase.getInstance(this)?.productDao()
        if(productdao?.isExist(productId)!=null){
            binding.productDetailAddToCartBtn.text = "Go to Cart"
        }
        else{
            binding.productDetailAddToCartBtn.text = "Add to Cart"
        }

        binding.productDetailAddToCartBtn.setOnClickListener{
            if(productdao?.isExist(productId)!=null){
               openCart()
            }
            else{
                addToCart(productId, productName, productMrp, productSp, productCoverImage, productdao)
            }
        }
    }

    private fun addToCart(
        productId: String,
        productName: String?,
        productMrp: String?,
        productSp: String?,
        productCoverImage: String?,
        productdao: ProductDao?
    ) {
     val data = ProductModel(productId, productName, productMrp, productSp, productCoverImage)
        lifecycleScope.launch {
            productdao?.insertProduct(data)
            binding.productDetailAddToCartBtn.text = "Go to cart"
        }
    }

    private fun openCart() {
      val preference = getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}