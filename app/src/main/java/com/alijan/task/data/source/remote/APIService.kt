package com.alijan.task.data.source.remote

import com.alijan.task.data.model.ApiResponse
import com.alijan.task.data.model.Category
import com.alijan.task.data.model.Product
import com.alijan.task.utils.Constant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("items")
    suspend fun getAllItems(@Query("key") key: String = Constant.API_KEY): Response<List<Product>>

    @POST("items")
    suspend fun addProduct(@Body item: Product): Response<ApiResponse>

    @PUT("items")
    suspend fun updateProduct(@Body item: Product): Response<ApiResponse>

    @DELETE("items/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<ApiResponse>

    @GET("categories")
    suspend fun getAllCategories(): Response<List<Category>>
}