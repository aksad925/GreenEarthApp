package com.lifegreen.greenearthapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lifegreen.greenearthapp.activity.ProductDetailsActivity
import com.lifegreen.greenearthapp.databinding.LayoutCartItemBinding
import com.lifegreen.greenearthapp.roomdb.AppDatabase
import com.lifegreen.greenearthapp.roomdb.ProductModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(val context: Context, val cartItemList: List<ProductModel>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: LayoutCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            LayoutCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val data = cartItemList[position]
        Glide.with(context).load(data.productImage).into(holder.binding.productCartImageview)
        holder.binding.productCartName.text = data.productName
        holder.binding.productCartMrp.text = "₹"+data.productMrp
        holder.binding.productCartMrp.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.productCartSp.text = "₹"+data.productSp

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id", data.productId)
            context.startActivity(intent)
        }

        val dao = AppDatabase.getInstance(context)?.productDao()
        holder.binding.productCartDelete.setOnClickListener{
            GlobalScope.launch {
                dao?.deleteProduct(
                    ProductModel(
                        data.productId,
                        data.productName,
                        data.productMrp,
                        data.productSp,
                        data.productImage
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }
}