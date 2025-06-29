package com.example.dorothy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.empleado.Empleado
import com.example.dorothy.empleado.EmpleadoDBHelper


class MainActivity : AppCompatActivity(){
    private lateinit var empleadoDBHelper: EmpleadoDBHelper
    private lateinit var usuarioNombre : EditText
    private lateinit var usuarioContrasenia : EditText
    private lateinit var btnIniciarSesion : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        empleadoDBHelper = EmpleadoDBHelper()

        usuarioNombre = findViewById(R.id.txtNombreUser)
        usuarioContrasenia = findViewById(R.id.txtContraseniaUser)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)

        btnIniciarSesion.setOnClickListener{
            val empleado = Empleado(
                "", usuarioNombre.text.toString(),"","",
                usuarioContrasenia.text.toString()
            )
            empleadoDBHelper.verificarEmpleado(empleado.nombre, empleado.contrasenia){ resultado ->
                if(resultado){
                     val intent = Intent(this, GestorAlmacenActivity()::class.java)
                    intent.putExtra("usuario", empleado.nombre)
                    intent.putExtra("rol", empleado.rol)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Debes ingresar los datos de un empleado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
