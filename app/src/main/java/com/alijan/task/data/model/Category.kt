package com.alijan.task.data.model


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
)