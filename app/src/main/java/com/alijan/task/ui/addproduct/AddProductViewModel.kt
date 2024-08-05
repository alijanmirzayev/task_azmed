package com.alijan.task.ui.addproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alijan.task.data.model.Category
import com.alijan.task.data.model.Product
import com.alijan.task.data.repository.ProductRepository
import com.alijan.task.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private var _categories = MutableLiveData<List<Category>>()
    val items: LiveData<List<Category>> get() = _categories

    private var _isAdded = MutableLiveData<Boolean>()
    val isAdded: LiveData<Boolean> get() = _isAdded

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    init {
        getAllCategories()
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            _loading.value = true
            productRepository.getAllCategories().collect { response ->
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
                            _categories.value = it
                        }
                    }
                }
                _loading.value = false
            }
        }
    }

    fun addProduct(item: Product) {
        viewModelScope.launch {
            _loading.value = true
            productRepository.addProduct(item).collect { response ->
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
                            _isAdded.value = true
                        }
                    }
                }
                _loading.value = false
            }
        }
    }


}