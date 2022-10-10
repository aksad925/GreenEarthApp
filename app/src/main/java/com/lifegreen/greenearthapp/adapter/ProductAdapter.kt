package com.lifegreen.greenearthapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lifegreen.greenearthapp.activity.ProductDetailsActivity
import com.lifegreen.greenearthapp.databinding.LayoutProductItemBinding
import com.lifegreen.greenearthapp.model.ProductModel

class ProductAdapter(private val context: Context, private val productList: ArrayList<ProductModel>): RecyclerView.Adapter<ProductAdapter.AddProductViewHolder>() {
   inner  class AddProductViewHolder(val binding: LayoutProductItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProductViewHolder {
        val binding = LayoutProductItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return AddProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddProductViewHolder, position: Int) {
        val data = productList[position]
        Glide.with(context).load(data.productCoverImage).into(holder.binding.productImageview)
        holder.binding.productNameTextView.text = data.productName
        holder.binding.productCategoryTextview.text =data.productCategory
        holder.binding.productMrpTextview.text=data.productMrp
        holder.binding.productSpBtn.text = data.productSp
      holder.itemView.setOnClickListener{
          val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtra("id", productList[position].productId)
        context.startActivity(intent)
      }
    }

    override fun getItemCount(): Int {
       return productList.size
    }
}