package com.alijan.task.ui.addproduct

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.alijan.task.data.model.Category
import com.alijan.task.data.model.Product
import com.alijan.task.databinding.FragmentAddProductBinding
import com.alijan.task.ui.BaseFragment
import com.alijan.task.utils.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment : BaseFragment<FragmentAddProductBinding>() {
    private val viewModel by viewModels<AddProductViewModel>()
    private var selectedCategory = 1

    override fun layoutInflater(): FragmentAddProductBinding =
        FragmentAddProductBinding.inflate(layoutInflater)

    override fun setupUI() {
        observeData()
        buttonClick()
    }

    private fun observeData() {
        viewModel.items.observe(viewLifecycleOwner) {
            setupSpinner(it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.loading.observe(viewLifecycleOwner) {

        }
        viewModel.isAdded.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Məhsul uğurla əlavə edildi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buttonClick() {
        with(binding) {
            buttonAddProduct.setOnClickListener {
                val name = editTextAddProductName.text.toString().trim()
                val amount = editTextAddProductAmount.text.toString().trim().toDouble()
                val note = editTextAddProductNote.text.toString().trim()
                val category = selectedCategory
                if (name.isNotEmpty() && !amount.isNaN() && category != null) {
                    val item = Product(
                        name = name,
                        amount = amount,
                        note = note,
                        category = category,
                        key = Constant.API_KEY
                    )
                    viewModel.addProduct(item)
                }
            }
        }
    }

    private fun setupSpinner(categories: List<Category>) {
        val categoryNames = categories.map { it.name }
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerAddProduct.adapter = adapter
        binding.spinnerAddProduct.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    categories[position].id?.let {
                        selectedCategory = it.toInt()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
    }

}