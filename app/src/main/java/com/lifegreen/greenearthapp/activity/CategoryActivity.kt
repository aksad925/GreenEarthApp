package com.lifegreen.greenearthapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lifegreen.greenearthapp.R
import com.lifegreen.greenearthapp.adapter.CategoryProductAdapter
import com.lifegreen.greenearthapp.model.ProductModel

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        getProduct(intent.getStringExtra("cat"))
    }

    private fun getProduct(category: String?) {
      val list = ArrayList<ProductModel>()
        Firebase.firestore.collection("Products").whereEqualTo( "productCategory", category)
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents){
                    doc.toObject(ProductModel::class.java)?.let { data -> list.add(data) }
                }
                val recyclerView = findViewById<RecyclerView>(R.id.category_recycler_view)
                recyclerView.adapter = CategoryProductAdapter(this, list)
            }
    }
}