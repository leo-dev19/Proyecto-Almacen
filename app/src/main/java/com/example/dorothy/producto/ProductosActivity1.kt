package com.example.dorothy

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.producto.ProductoDBHelper
import com.example.dorothy.producto.ProductosActivity


class ProductosActivity1 : AppCompatActivity() {
    private lateinit var etNombre: EditText
    private lateinit var etCategoria: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etStock: EditText
    private lateinit var btnGuardar: Button

    private lateinit var dbHelper: ProductoDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_producto)

        dbHelper = ProductoDBHelper(this)

        etNombre = findViewById(R.id.etNombre)
        etCategoria = findViewById(R.id.etCategoria)
        etPrecio = findViewById(R.id.etPrecio)
        etStock = findViewById(R.id.etStock)
        btnGuardar = findViewById(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            guardarProducto()
        }

    }

    private fun guardarProducto() {
        val nombre = etNombre.text.toString()
        val categoria = etCategoria.text.toString()
        val precio = etPrecio.text.toString().toDoubleOrNull()
        val stock = etStock.text.toString().toIntOrNull()

        if (nombre.isEmpty() || categoria.isEmpty() || precio == null || stock == null) {
            Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ProductoDBHelper.COL_NOMBRE, nombre)
            put(ProductoDBHelper.COL_CATEGORIA, categoria)
            put(ProductoDBHelper.COL_PRECIO, precio)
            put(ProductoDBHelper.COL_STOCK, stock)
        }

        val id = db.insert(ProductoDBHelper.TABLE_PRODUCTO, null, values)
        if (id > 0) {
            Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show()
            limpiarCampos()
            val intent = Intent(this, ProductosActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        etNombre.text.clear()
        etCategoria.text.clear()
        etPrecio.text.clear()
        etStock.text.clear()
    }


}
