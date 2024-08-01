package com.bratuha.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShoppingListAdapter(
    private val items: List<ShoppingItem>,
    private val onItemChecked: (Int) -> Unit,
    private val onItemDeleted: (Int) -> Unit
) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemCheckBox: CheckBox = view.findViewById(R.id.item_checkbox)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name
        holder.itemCheckBox.isChecked = item.isPurchased
        holder.itemCheckBox.setOnClickListener { onItemChecked(position) }
        holder.deleteButton.setOnClickListener { onItemDeleted(position) }
    }

    override fun getItemCount() = items.size
}