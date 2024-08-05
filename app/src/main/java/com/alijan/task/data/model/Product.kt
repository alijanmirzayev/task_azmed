package com.alijan.task.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("category")
    val category: Int?,
    @SerializedName("key")
    val key: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("note")
    val note: String?
): Parcelable