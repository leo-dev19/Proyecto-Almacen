package com.example.dorothy.empleado

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.R


class EmpleadoAgregarActivity : AppCompatActivity() {
    private lateinit var empleadoDBHelper: EmpleadoDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_empleado_agregar)
        empleadoDBHelper = EmpleadoDBHelper(this)

        var btnAgregar : Button = findViewById(R.id.btnConfirmarAgregar)
        var btnRegresar : Button = findViewById(R.id.btnRegresarEmpleados)

        findViewById<EditText>(R.id.txtNombreEmpleado).setOnClickListener {
            Toast.makeText(this, findViewById<Spinner>(R.id.cbxRol).selectedItem.toString(), Toast.LENGTH_SHORT).show()
        }

        btnAgregar.setOnClickListener {
            var empleado = Empleado(
                nombre = findViewById<EditText>(R.id.txtNombreEmpleado).text.toString(),
                apellido = findViewById<EditText>(R.id.txtApellidoEmpleado).text.toString(),
                rol = findViewById<Spinner>(R.id.cbxRol).selectedItem.toString(),
                contrasenia = findViewById<EditText>(R.id.txtContraseniaEmpleado).text.toString(),
            )

            if(validarEmpleado(empleado)){
                try {
                    empleadoDBHelper.insertarEmpleado(empleado)
                    intent = Intent(this, EmpleadoActivity::class.java)
                    startActivity(intent)
                }catch (ex: Exception){
                    Toast.makeText(this, "A ocurrido un error al agregar", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Debe ingresar datos validos", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegresar.setOnClickListener {
            intent = Intent(this, EmpleadoActivity::class.java)
            startActivity(intent)
        }
    }

    fun validarEmpleado(empleado: Empleado) : Boolean{
        if(empleado.nombre == "") return false
        if(empleado.apellido == "")return false
        if(empleado.contrasenia == "")return false
        return true
    }
}