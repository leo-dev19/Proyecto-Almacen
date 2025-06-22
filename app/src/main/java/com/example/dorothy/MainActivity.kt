package com.example.dorothy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.empleado.Empleado
import com.example.dorothy.empleado.EmpleadoActivity
import com.example.dorothy.empleado.EmpleadoDBHelper

class MainActivity : AppCompatActivity() {
    private lateinit var empleadoDBHelper: EmpleadoDBHelper
    private lateinit var usuarioNombre : EditText
    private lateinit var usuarioContrasenia : EditText
    private lateinit var btnIniciarSesion : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        empleadoDBHelper = EmpleadoDBHelper(this)

        usuarioNombre = findViewById(R.id.txtNombreUser)
        usuarioContrasenia = findViewById(R.id.txtContraseniaUser)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)


        btnIniciarSesion.setOnClickListener{
            val empleado = Empleado(
                0, usuarioNombre.text.toString(),"","",
                usuarioContrasenia.text.toString()
            )

            if(empleadoDBHelper.verificarEmpleado(empleado.nombre, empleado.contrasenia)){
                intent = Intent(this, EmpleadoActivity::class.java)
                intent.putExtra("usuario", empleado.nombre)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Debes ingresar los datos del empleado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}