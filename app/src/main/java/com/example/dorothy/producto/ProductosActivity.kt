package com.example.dorothy.producto

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dorothy.R
import com.example.dorothy.menu.MenuActivity

class ProductosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private lateinit var dbHelper: ProductoDBHelper
    private lateinit var etBuscar: EditText
    private lateinit var btnBuscar: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        recyclerView = findViewById(R.id.rvProductos)
        etBuscar = findViewById(R.id.etBuscar)
        btnBuscar = findViewById(R.id.btnBuscarProducto)

        recyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = ProductoDBHelper(this)

        val listaProductos = dbHelper.obtenerProductos().toMutableList()

        adapter = ProductoAdapter(
            listaProductos,
            onEditarClick = { producto ->
                Toast.makeText(this, "Editar: ${producto.nombre}", Toast.LENGTH_SHORT).show()
            },
            onEliminarClick = { producto, position ->
                dbHelper.eliminarProducto(producto.id)
                adapter.eliminarProducto(position)
                Toast.makeText(this, "Producto eliminado: ${producto.nombre}", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.adapter = adapter

        btnBuscar.setOnClickListener {
            val texto = etBuscar.text.toString().trim()
            val resultados = if (texto.isEmpty()) {
                dbHelper.obtenerProductos()
            } else {
                dbHelper.buscarPorNombre(texto)
            }

            adapter = ProductoAdapter(
                resultados.toMutableList(),
                onEditarClick = { producto ->
                    Toast.makeText(this, "Editar: ${producto.nombre}", Toast.LENGTH_SHORT).show()
                },
                onEliminarClick = { producto, position ->
                    dbHelper.eliminarProducto(producto.id)
                    adapter.eliminarProducto(position)
                    Toast.makeText(this, "Producto eliminado: ${producto.nombre}", Toast.LENGTH_SHORT).show()
                }
            )
            recyclerView.adapter = adapter
        }

        Toast.makeText(this, "Bienvenido a Listado de Productos", Toast.LENGTH_SHORT).show()
    }

}
