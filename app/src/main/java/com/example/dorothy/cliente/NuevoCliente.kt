package com.example.dorothy.cliente

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.R

class NuevoCliente : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCelular: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnGrabar: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var dbHelper: ClienteDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_cliente)

        etNombre = findViewById(R.id.etNombre)
        etCelular = findViewById(R.id.etCelular)
        etEmail = findViewById(R.id.etEmail)
        btnGrabar = findViewById(R.id.btnGrabar)
        progressBar = findViewById(R.id.progressBar)

        dbHelper = ClienteDBHelper()  // Firestore no necesita Context

        btnGrabar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val celular = etCelular.text.toString().trim()
            val email = etEmail.text.toString().trim()

            val faltan = mutableListOf<String>()
            if (nombre.isEmpty()) faltan.add("Nombre")
            if (celular.isEmpty()) faltan.add("Celular")
            if (email.isEmpty()) faltan.add("Email")

            if (faltan.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Campos requeridos")
                    .setMessage("Faltan: ${faltan.joinToString(", ")}")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Desea grabar al cliente?")
                .setPositiveButton("Sí") { _, _ ->
                    progressBar.visibility = View.VISIBLE

                    // Puedes quitar el delay, Firestore ya es asíncrono
                    val cliente = Cliente(
                        nombre = nombre,
                        telefono = celular,
                        email = email
                    )
                    dbHelper.insertarCliente(cliente) { success ->
                        progressBar.visibility = View.GONE
                        if (success) {
                            Toast.makeText(this, "Cliente registrado", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Error al registrar cliente", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}
