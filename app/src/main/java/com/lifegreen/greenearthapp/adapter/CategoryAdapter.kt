package com.lifegreen.greenearthapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lifegreen.greenearthapp.R
import com.lifegreen.greenearthapp.activity.CategoryActivity
import com.lifegreen.greenearthapp.databinding.LayoutCategoryItemBinding
import com.lifegreen.greenearthapp.model.CategoryModel

class CategoryAdapter(val context: Context, private val categoryItems: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
      return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item, parent,false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.categoryName.text = categoryItems[position].cat
        Glide.with(context).load(categoryItems[position].img).into(holder.binding.categoryImageView)
        holder.itemView.setOnClickListener{
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra("cat", categoryItems[position].cat)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return categoryItems.size
    }

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = LayoutCategoryItemBinding.bind(view)
    }

}