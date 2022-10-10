package com.lifegreen.greenearthapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lifegreen.greenearthapp.activity.ProductDetailsActivity
import com.lifegreen.greenearthapp.databinding.ItemCategoryProductLayoutBinding
import com.lifegreen.greenearthapp.model.ProductModel

class CategoryProductAdapter(
    private val context: Context,
    private val productList: ArrayList<ProductModel>
) :
    RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder>() {
    inner class CategoryProductViewHolder(val binding: ItemCategoryProductLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
        val binding =
            ItemCategoryProductLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
        val data = productList[position]
        Glide.with(context).load(data.productCoverImage)
            .into(holder.binding.productImageViewCategory)
        holder.binding.productNameTextViewCategory.text = data.productName
        holder.binding.productMrpTextviewCategory.text = data.productMrp
        holder.binding.productMrpTextviewCategory.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.productSpTextviewCategory.text = data.productSp
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id", productList[position].productId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}