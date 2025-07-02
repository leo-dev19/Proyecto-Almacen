package com.example.dorothy.producto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dorothy.R
import com.example.myapplication.ProductoAdapter


class ListarProductosActivity : AppCompatActivity() {

    private lateinit var recyclerProductos: RecyclerView
    private lateinit var dbHelper: ProductoDBHelper
    private lateinit var adapter: ProductoAdapter
    private lateinit var etBuscar: EditText
    private lateinit var btnBuscar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        recyclerProductos = findViewById(R.id.rvProductos)
        recyclerProductos.layoutManager = LinearLayoutManager(this)

        etBuscar = findViewById(R.id.etBuscar)
        btnBuscar = findViewById(R.id.btnBuscar)

        dbHelper = ProductoDBHelper()

        adapter = ProductoAdapter(this, mutableListOf(), dbHelper)
        recyclerProductos.adapter = adapter

        cargarProductos()

        btnBuscar.setOnClickListener {
            val nombreBuscado = etBuscar.text.toString().trim()
            if (nombreBuscado.isNotEmpty()) {
                dbHelper.obtenerProductos { lista ->
                    val filtrados = lista.filter {
                        it.nombre?.contains(nombreBuscado, ignoreCase = true) ?: false
                    }
                    adapter.actualizarLista(filtrados.toMutableList())
                }
            } else {
                cargarProductos()
            }
        }


        val btnNuevoProducto = findViewById<Button>(R.id.btnNuevoProducto)
        btnNuevoProducto.setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    private fun cargarProductos() {
        dbHelper.obtenerProductos { lista ->
            adapter.actualizarLista(lista.toMutableList())
        }
    }
}
