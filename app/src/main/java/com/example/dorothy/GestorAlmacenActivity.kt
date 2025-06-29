package com.example.dorothy

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.empleado.EmpleadoActivity
import com.example.dorothy.lotes.GestionLotesActivity

class GestorAlmacenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val cardLotes = findViewById<LinearLayout>(R.id.cardLotes)
        val cardEmpleados = findViewById<LinearLayout>(R.id.cardEmpleados)
        val cardProductos = findViewById<LinearLayout>(R.id.cardProductos)
        val cardAlmacenes = findViewById<LinearLayout>(R.id.cardAlmacenes)

        intent.putExtra("nombre", intent.getStringExtra("nombre"))
        intent.putExtra("rol", intent.getStringExtra("rol"))

        cardLotes.setOnClickListener {
            startActivity(Intent(this, GestionLotesActivity::class.java))
        }

        cardEmpleados.setOnClickListener {
            intent = Intent(this, EmpleadoActivity::class.java)
            startActivity(intent)
        }

        cardProductos.setOnClickListener {
            // Intent para GestionProductosActivity
        }

        cardAlmacenes.setOnClickListener {
            // Intent para GestionAlmacenesActivity
        }
    }
}
