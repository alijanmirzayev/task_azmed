package com.alijan.task.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alijan.task.data.model.Product
import com.alijan.task.data.repository.ProductRepository
import com.alijan.task.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private var _items = MutableLiveData<List<Product>>()
    val items: LiveData<List<Product>> get() = _items

    private var _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> get() = _isDeleted

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    init {
        getAllItems()
    }

    fun getAllItems() {
        viewModelScope.launch {
            _loading.value = true
            productRepository.getAllItems().collect { response ->
                when (response) {
                    is NetworkResponse.Error -> {
                        response.message?.let {
                            _error.value = it
                        } ?: {
                            _error.value = "Unknown Error"
                        }
                    }

                    is NetworkResponse.Success -> {
                        response.data?.let {
                            _items.value = it
                        }
                    }
                }
                _loading.value = false
            }
        }
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            productRepository.deleteProduct(id).collect { response ->
                when (response) {
                    is NetworkResponse.Error -> {
                        response.message?.let {
                            _error.value = it
                        } ?: {
                            _error.value = "Unknown Error"
                        }
                    }

                    is NetworkResponse.Success -> {
                        response.data?.let {
                            _isDeleted.value = true
                        }
                    }
                }
            }
        }
    }

    fun clearData() {
        _items.value = emptyList()
        _isDeleted.value = false
    }

}