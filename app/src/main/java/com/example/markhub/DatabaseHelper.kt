package com.example.markhub

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

data class Product(val id: Int, val name: String, val price: Double)

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "products.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL)")
        Log.d("DatabaseHelper", "Base de datos y tabla 'products' creadas")
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS products")
        onCreate(db)
    }

    fun insertProduct(product: Product): Long {
        val values = ContentValues().apply {
            put("name", product.name)
            put("price", product.price)
        }
        val result = writableDatabase.insert("products", null, values)
        if (result != -1L) {
            Log.d("DatabaseHelper", "Producto insertado con éxito: ${product.name}")
        } else {
            Log.e("DatabaseHelper", "Error al insertar el producto: ${product.name}")
        }
        return result
    }


    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM products", null)
        if (cursor.moveToFirst()) {
            do {
                products.add(Product(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return products
    }

    fun updateProduct(product: Product): Int {
        val values = ContentValues().apply {
            put("name", product.name)
            put("price", product.price)
        }
        return writableDatabase.update("products", values, "id = ?", arrayOf(product.id.toString()))
    }

    fun deleteProduct(id: Int): Int {
        return writableDatabase.delete("products", "id = ?", arrayOf(id.toString()))
    }
}
