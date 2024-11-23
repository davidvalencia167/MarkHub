package com.example.markhub

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.markhub.databinding.ActivityProductFormBinding

class ProductFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductFormBinding
    private lateinit var databaseHelper: DatabaseHelper

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)

        if (productId != -1) {
            val product = databaseHelper.getAllProducts().find { it.id == productId }
            if (product != null) {
                binding.etName.setText(product.name)
                binding.etPrice.setText(product.price.toString())
            }
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val price = binding.etPrice.text.toString().toDoubleOrNull()

            if (name.isNotBlank() && price != null) {
                if (productId == -1) {
                    databaseHelper.insertProduct(Product(0, name, price))
                    Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
                } else {
                    databaseHelper.updateProduct(Product(productId, name, price))
                    Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDelete.setOnClickListener {
            if (productId != -1) {
                databaseHelper.deleteProduct(productId)
                Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
