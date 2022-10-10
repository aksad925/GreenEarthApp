package com.lifegreen.greenearthapp.roomdb

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductModel(
    @PrimaryKey
    @NonNull
    val productId:String,
    @ColumnInfo(name ="productName")
    val productName:String? ="",
    @ColumnInfo(name ="productMrp")
    val productMrp:String? ="",
    @ColumnInfo(name ="productSp")
    val productSp:String?="",
    @ColumnInfo(name ="productImage")
    val productImage:String? =""
)
