package com.bratuha.shoppinglist

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private lateinit var purchasedListAdapter: ShoppingListAdapter
    private var shoppingList = mutableListOf<ShoppingItem>()
    private var purchasedList = mutableListOf<ShoppingItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadLists()

        val shoppingRecyclerView: RecyclerView = findViewById(R.id.shopping_recycler_view)
        val purchasedRecyclerView: RecyclerView = findViewById(R.id.purchased_recycler_view)
        val addItemButton: ImageButton = findViewById(R.id.add_item_button)

        shoppingListAdapter = ShoppingListAdapter(shoppingList,
            onItemChecked = { position -> moveItemToPurchased(position) },
            onItemDeleted = { position -> deleteShoppingItem(position) }
        )
        purchasedListAdapter = ShoppingListAdapter(purchasedList,
            onItemChecked = { position -> moveItemToShopping(position) },
            onItemDeleted = { position -> deletePurchasedItem(position) }
        )

        shoppingRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = shoppingListAdapter
        }

        purchasedRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = purchasedListAdapter
        }

        addItemButton.setOnClickListener { showAddItemDialog() }
    }

    private fun showAddItemDialog() {
        val input = EditText(this)
        input.hint = "Enter item name"
        AlertDialog.Builder(this)
            .setTitle("Add Shopping Item")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val newItem = input.text.toString().trim()
                if (newItem.isNotEmpty()) {
                    addShoppingItem(newItem)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addShoppingItem(itemName: String) {
        val newItem = ShoppingItem(itemName, false)
        shoppingList.add(newItem)
        shoppingListAdapter.notifyItemInserted(shoppingList.size - 1)
        saveLists()
    }

    private fun moveItemToPurchased(position: Int) {
        val item = shoppingList.removeAt(position)
        item.isPurchased = true
        purchasedList.add(item)
        shoppingListAdapter.notifyItemRemoved(position)
        purchasedListAdapter.notifyItemInserted(purchasedList.size - 1)
        saveLists()
    }

    private fun moveItemToShopping(position: Int) {
        val item = purchasedList.removeAt(position)
        item.isPurchased = false
        shoppingList.add(item)
        purchasedListAdapter.notifyItemRemoved(position)
        shoppingListAdapter.notifyItemInserted(shoppingList.size - 1)
        saveLists()
    }

    private fun deleteShoppingItem(position: Int) {
        shoppingList.removeAt(position)
        shoppingListAdapter.notifyItemRemoved(position)
        saveLists()
    }

    private fun deletePurchasedItem(position: Int) {
        purchasedList.removeAt(position)
        purchasedListAdapter.notifyItemRemoved(position)
        saveLists()
    }

    private fun saveLists() {
        val sharedPreferences = getSharedPreferences("ShoppingListPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        editor.putString("shopping_list", gson.toJson(shoppingList))
        editor.putString("purchased_list", gson.toJson(purchasedList))
        editor.apply()
    }

    private fun loadLists() {
        val sharedPreferences = getSharedPreferences("ShoppingListPrefs", MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<List<ShoppingItem>>() {}.type

        val shoppingListJson = sharedPreferences.getString("shopping_list", null)
        shoppingList = if (shoppingListJson != null) {
            gson.fromJson(shoppingListJson, type)
        } else {
            mutableListOf()
        }

        val purchasedListJson = sharedPreferences.getString("purchased_list", null)
        purchasedList = if (purchasedListJson != null) {
            gson.fromJson(purchasedListJson, type)
        } else {
            mutableListOf()
        }
    }
}