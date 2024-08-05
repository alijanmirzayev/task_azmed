package com.alijan.task.ui.editproduct

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.alijan.task.data.model.Category
import com.alijan.task.data.model.Product
import com.alijan.task.databinding.FragmentEditProductBinding
import com.alijan.task.ui.BaseFragment
import com.alijan.task.utils.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProductFragment : BaseFragment<FragmentEditProductBinding>() {
    private val args: EditProductFragmentArgs by navArgs()
    private val viewModel by viewModels<EditProductViewModel>()
    private var selectedCategory = 1

    override fun layoutInflater(): FragmentEditProductBinding =
        FragmentEditProductBinding.inflate(layoutInflater)

    override fun setupUI() {
        setData()
        observeData()
        buttonClick()
    }

    private fun setData(){
        with(binding){
            val item = args.item
            editTextEditProductName.setText(item.name)
            editTextEditProductAmount.setText(item.amount.toString())
            editTextEditProductNote.setText(item.note)
        }
    }

    private fun observeData() {
        viewModel.items.observe(viewLifecycleOwner) {
            setupSpinner(it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.isEdited.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Məhsul uğurla düzəliş edildi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buttonClick() {
        with(binding) {
            buttonEditProduct.setOnClickListener {

                val name = editTextEditProductName.text.toString().trim()
                val amount = editTextEditProductAmount.text.toString().trim().toDouble()
                val note = editTextEditProductNote.text.toString().trim()
                val category = selectedCategory
                if (name.isNotEmpty() && !amount.isNaN() && category != null) {
                    val item = Product(
                        id= args.item.id,
                        name = name,
                        amount = amount,
                        note = note,
                        category = category,
                        key = Constant.API_KEY
                    )

                    viewModel.updateProduct(item)
                }
            }
        }
    }

    private fun setupSpinner(categories: List<Category>) {
        val categoryNames = categories.map { it.name }
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerEditProduct.adapter = adapter

        val selectedPosition = categories.indexOfFirst { it.id == args.item.category.toString() }
        selectedCategory = selectedPosition
        if (selectedPosition != -1) {
            binding.spinnerEditProduct.setSelection(selectedPosition)
        }

        binding.spinnerEditProduct.onItemSelectedListener =
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