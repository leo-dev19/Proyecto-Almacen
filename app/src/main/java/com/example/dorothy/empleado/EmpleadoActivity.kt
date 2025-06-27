package com.example.dorothy.empleado

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dorothy.MainActivity
import com.example.dorothy.R
import com.google.firebase.firestore.FirebaseFirestore

class EmpleadoActivity() : AppCompatActivity() {
    private lateinit var empleadoDBHelper: EmpleadoDBHelper
    private lateinit var contenedorEmpleado: RecyclerView
    private val firebaseInstancia = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_empleado)

        empleadoDBHelper = EmpleadoDBHelper()
        cargarListaEmpleados(null)

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

        var btnBuscar : Button = findViewById(R.id.btnBuscar)
        btnBuscar.setOnClickListener {
            var nombreEmpleado = findViewById<EditText>(R.id.txtBuscarNombre).text
            if(nombreEmpleado == null || nombreEmpleado.toString().isEmpty()){
                cargarListaEmpleados(null)
                Toast.makeText(this, "Ingrese un nombre de empleado", Toast.LENGTH_SHORT).show()
            }else{
                cargarListaEmpleados(nombreEmpleado.toString())
            }
        }
    }

    fun cargarListaEmpleados(nombre : String?) {
        var empleados = empleadoDBHelper.obtenerEmpleados(nombre).toMutableList()

        contenedorEmpleado = findViewById(R.id.listaEmpleados)
        contenedorEmpleado.layoutManager = LinearLayoutManager(this)
        var adaptador = EmpleadoAdapter(this, empleados){
            cargarListaEmpleados(null)
        }
        contenedorEmpleado.adapter = adaptador
    }
}