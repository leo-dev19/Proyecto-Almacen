package com.example.dorothy.producto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.R

class ProductosActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCategoria: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etStock: EditText
    private lateinit var btnGuardar: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var btnIrListado: Button

    private lateinit var dbHelper: ProductoDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_producto)

        etNombre = findViewById(R.id.etNombre)
        etCategoria = findViewById(R.id.etCategoria)
        etPrecio = findViewById(R.id.etPrecio)
        etStock = findViewById(R.id.etStock)
        btnGuardar = findViewById(R.id.btnGuardar)
        progressBar = findViewById(R.id.progressBar)
        btnIrListado = findViewById(R.id.btnIrListado)

        dbHelper = ProductoDBHelper()

        btnGuardar.setOnClickListener {
            validarYGuardar()
        }

        btnIrListado.setOnClickListener {
            val intent = Intent(this, ListarProductosActivity::class.java)
            startActivity(intent)
        }

        Toast.makeText(this, "Registro de Nuevo Producto", Toast.LENGTH_SHORT).show()
    }

    private fun validarYGuardar() {
        val nombre = etNombre.text.toString().trim()
        val categoria = etCategoria.text.toString().trim()
        val precioStr = etPrecio.text.toString().trim()
        val stockStr = etStock.text.toString().trim()

        val faltantes = mutableListOf<String>()
        if (nombre.isEmpty()) faltantes.add("Nombre")
        if (categoria.isEmpty()) faltantes.add("Categoría")
        if (precioStr.isEmpty()) faltantes.add("Precio")
        if (stockStr.isEmpty()) faltantes.add("Stock")

        if (faltantes.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Campos requeridos")
                .setMessage("Faltan: ${faltantes.joinToString(", ")}")
                .setPositiveButton("OK", null)
                .show()
            return
        }

        val precio = precioStr.toDoubleOrNull()
        val stock = stockStr.toIntOrNull()

        if (precio == null || stock == null) {
            Toast.makeText(this, "Precio y Stock deben ser numéricos válidos", Toast.LENGTH_SHORT).show()
            return
        }

        val producto = Producto(
            nombre = nombre,
            categoria = categoria,
            precio = precio.toString(),
            stock = stock.toString()
        )

        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage("¿Deseas registrar este producto?")
            .setPositiveButton("Sí") { _, _ ->
                progressBar.visibility = View.VISIBLE
                dbHelper.insertarProducto(producto) { success ->
                    progressBar.visibility = View.GONE
                    if (success) {
                        Toast.makeText(this, "Producto registrado correctamente", Toast.LENGTH_SHORT).show()
                        limpiarCampos()
                    } else {
                        Toast.makeText(this, "Error al registrar el producto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun limpiarCampos() {
        etNombre.text.clear()
        etCategoria.text.clear()
        etPrecio.text.clear()
        etStock.text.clear()
    }
}
