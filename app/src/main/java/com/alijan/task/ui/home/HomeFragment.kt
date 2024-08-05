package com.alijan.task.ui.home

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.alijan.task.databinding.FragmentHomeBinding
import com.alijan.task.ui.BaseFragment
import com.alijan.task.ui.adapter.ProductAdapter
import com.alijan.task.utils.SwipeCallback
import com.alijan.task.utils.gone
import com.alijan.task.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel by viewModels<HomeViewModel>()
    private val productAdapter = ProductAdapter()

    override fun layoutInflater(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun setupUI() {
        setupAdapter()
        observeData()
        buttonClick()
    }

    private fun setupAdapter() {
        binding.rvHomeProduct.adapter = productAdapter

        val swipeCallback = SwipeCallback(
            onDelete = { position ->
                viewModel.items.value?.let {
                    val (id) = it[position]
                    viewModel.deleteProduct(id.toString())
                    productAdapter.removeItem(position)
                }
            },
            onEdit = { position ->
                viewModel.items.value?.let {
                    val item = it[position]
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToEditProductFragment(
                            item = item
                        )
                    )
                }
            }
        )

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvHomeProduct)

    }

    private fun observeData() {
        viewModel.items.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                productAdapter.updateList(it)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visible()
            } else {
                binding.progressBar.gone()
            }
        }
        viewModel.isDeleted.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Məhsul uğurla silindi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buttonClick() {
        binding.buttonHomeAddProduct.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddProductFragment())
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllItems()
    }

}