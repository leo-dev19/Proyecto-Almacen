package com.example.dorothy.producto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dorothy.R

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
        dbHelper = ProductoDBHelper(this)

        recyclerProductos.layoutManager = LinearLayoutManager(this)
        cargarProductos()

        etBuscar = findViewById(R.id.etBuscar)
        btnBuscar = findViewById(R.id.btnBuscar)

        btnBuscar.setOnClickListener {
            val nombreBuscado = etBuscar.text.toString().trim()
            if (nombreBuscado.isNotEmpty()) {
                val resultados = dbHelper.buscarPorNombre(nombreBuscado)
                adapter.actualizarLista(resultados.toMutableList())
            } else {
                cargarProductos() // Si está vacío, carga todo
            }
        }


        val btnNuevoProducto = findViewById<Button>(R.id.btnNuevoProducto)
        btnNuevoProducto.setOnClickListener {
            val intent = Intent(this, ProductosActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    private fun cargarProductos() {
        val listaProductos = dbHelper.obtenerProductos()
        adapter = ProductoAdapter(this, listaProductos.toMutableList(), dbHelper)
        recyclerProductos.adapter = adapter
    }
}
