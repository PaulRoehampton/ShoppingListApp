package com.bratuha.shoppinglist

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShoppingListManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("ShoppingList", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveShoppingList(list: List<ShoppingItem>) {
        val json = gson.toJson(list)
        sharedPreferences.edit().putString("shopping_list", json).apply()
    }

    fun loadShoppingList(): List<ShoppingItem> {
        val json = sharedPreferences.getString("shopping_list", null)
        return if (json != null) {
            val type = object : TypeToken<List<ShoppingItem>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}