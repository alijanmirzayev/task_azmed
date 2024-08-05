package com.alijan.task.data.repository

import com.alijan.task.data.model.Product
import com.alijan.task.data.source.remote.APIService
import com.alijan.task.utils.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(private val apiService: APIService) {

    private suspend fun <T> safeApiRequest(request: suspend () -> Response<T>) = flow {
        try {
            val response = request.invoke()
            if (response.isSuccessful) {
                emit(NetworkResponse.Success(response.body()))
            } else {
                emit(NetworkResponse.Error(response.errorBody()?.string() ?: "Unknown Error"))
            }
        } catch (e: Exception) {
            emit(NetworkResponse.Error(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun getAllItems() = safeApiRequest {
        apiService.getAllItems()
    }

    suspend fun getAllCategories() = safeApiRequest {
        apiService.getAllCategories()
    }

    suspend fun addProduct(item: Product) = safeApiRequest {
        apiService.addProduct(item)
    }

    suspend fun updateProduct(item: Product) = safeApiRequest {
        apiService.updateProduct(item)
    }

    suspend fun deleteProduct(id: String) = safeApiRequest {
        apiService.deleteProduct(id)
    }

}