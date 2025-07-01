package com.example.dorothy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dorothy.cliente.ClienteDBHelper
import com.example.dorothy.cliente.NuevoCliente
import com.example.myapplication.ClienteAdapter
import com.google.firebase.FirebaseApp

class ListarClientesActivity : AppCompatActivity() {

    private lateinit var recyclerClientes: RecyclerView
    private lateinit var dbHelper: ClienteDBHelper
    private lateinit var adapter: ClienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_cliente)

        // ✅ Inicializa Firebase SOLO para esta Activity de prueba
        FirebaseApp.initializeApp(this)

        recyclerClientes = findViewById(R.id.recyclerClientes)
        dbHelper = ClienteDBHelper() // 👈 Firestore no necesita Context

        recyclerClientes.layoutManager = LinearLayoutManager(this)
        cargarClientes()

        val btnNuevoCliente = findViewById<Button>(R.id.btnNuevoCliente)
        btnNuevoCliente.setOnClickListener {
            val intent = Intent(this, NuevoCliente::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarClientes()
    }

    private fun cargarClientes() {
        dbHelper.obtenerClientes { listaClientes ->
            adapter = ClienteAdapter(this, listaClientes.toMutableList(), dbHelper)
            recyclerClientes.adapter = adapter
        }
    }
}
