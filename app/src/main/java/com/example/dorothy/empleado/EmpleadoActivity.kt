package com.example.dorothy.empleado

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.MainActivity
import com.example.dorothy.R

class EmpleadoActivity : AppCompatActivity() {
    private lateinit var empleadoDBHelper: EmpleadoDBHelper
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var empleados = mutableListOf<Empleado>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_empleado)

        listView = findViewById(R.id.listaEmpleados)
        empleadoDBHelper = EmpleadoDBHelper(this)
        cargarListaEmpleados()

        findViewById<TextView>(R.id.lblNombreUser).text = intent.getStringExtra("usuario")

        var btnAgregarEmpleado : Button = findViewById(R.id.btnAgregarEmpleado)
        btnAgregarEmpleado.setOnClickListener {
            intent = Intent(this, EmpleadoAgregarActivity::class.java)
            startActivity(intent)
        }

        var btnRegresar : Button = findViewById(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun cargarListaEmpleados() {
        empleados = empleadoDBHelper.obtenerEmpleados(null).toMutableList()
        val nombres = empleados.map { "${it.nombre}, rol: ${it.rol}, Contra: ${ it.contrasenia }" }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombres)
        listView.adapter = adapter
    }
}