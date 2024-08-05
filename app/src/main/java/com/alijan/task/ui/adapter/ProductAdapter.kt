package com.alijan.task.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alijan.task.data.model.Product
import com.alijan.task.databinding.ItemProductBinding

class ProductAdapter() : RecyclerView.Adapter<ProductAdapter.ServiceViewHolder>() {

    private val itemList = ArrayList<Product>()

    inner class ServiceViewHolder(val itemProductBinding: ItemProductBinding) :
        RecyclerView.ViewHolder(itemProductBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val currentItem = itemList[position]
        val category = when (currentItem.category) {
            1 -> "Qida"
            2 -> "Tikinti"
            3 -> "Digər"
            else -> "Təyin edilmədi"
        }
        holder.itemProductBinding.item = currentItem
        holder.itemProductBinding.category = category

    }

    fun updateList(newList: List<Product>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }
}