package com.example.markhub

import ProductAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.markhub.databinding.ActivityProductListBinding

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        // Set up RecyclerView
        productAdapter = ProductAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = productAdapter

        // Display all products
        loadProducts()

        // Button to add a new product
        binding.addButton.setOnClickListener {
            val productName = binding.productNameEditText.text.toString()
            val productPrice = binding.productPriceEditText.text.toString().toDoubleOrNull()

            if (productName.isNotEmpty() && productPrice != null) {
                val product = Product(0, productName, productPrice)
                val result = databaseHelper.insertProduct(product)
                if (result != -1L) {
                    Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
                    loadProducts() // Refresh the list
                } else {
                    Toast.makeText(this, "Error al agregar producto", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor ingrese un nombre y precio válidos", Toast.LENGTH_SHORT).show()
            }
        }

        // Example of updating a product
        binding.updateButton.setOnClickListener {
            // Here you would retrieve the updated data and update the product accordingly
            val product = Product(1, "Updated Product", 15.99)
            val rowsUpdated = databaseHelper.updateProduct(product)
            if (rowsUpdated > 0) {
                Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
                loadProducts() // Refresh the list
            } else {
                Toast.makeText(this, "Error al actualizar producto", Toast.LENGTH_SHORT).show()
            }
        }

        // Example of deleting a product
        binding.deleteButton.setOnClickListener {
            // For example, deleting product with id = 1
            val rowsDeleted = databaseHelper.deleteProduct(1)
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
                loadProducts() // Refresh the list
            } else {
                Toast.makeText(this, "Error al eliminar producto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadProducts() {
        val products = databaseHelper.getAllProducts()
        productAdapter.updateProducts(products)
    }
}
