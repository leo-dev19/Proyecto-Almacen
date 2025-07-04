package com.example.dorothy

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.almacen.AlmacenesActivity
import com.example.dorothy.empleado.EmpleadoActivity
import com.example.dorothy.lotes.GestionLotesActivity
import com.example.dorothy.producto.ProductosActivity

class GestorAlmacenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val cardLotes = findViewById<LinearLayout>(R.id.cardLotes)
        val cardEmpleados = findViewById<LinearLayout>(R.id.cardEmpleados)
        val cardProductos = findViewById<LinearLayout>(R.id.cardProductos)
        val cardAlmacenes = findViewById<LinearLayout>(R.id.cardAlmacenes)
        val cardClientes = findViewById<LinearLayout>(R.id.cardClientes)

        intent.putExtra("nombre", intent.getStringExtra("nombre"))
        intent.putExtra("rol", intent.getStringExtra("rol"))

        cardLotes.setOnClickListener {
            intent = Intent(this, GestionLotesActivity::class.java)
            startActivity(intent)
        }

        cardEmpleados.setOnClickListener {
            intent = Intent(this, EmpleadoActivity::class.java)
            startActivity(intent)
        }

        cardProductos.setOnClickListener {
            intent = Intent(this, ProductosActivity::class.java)
            startActivity(intent)        }

        cardAlmacenes.setOnClickListener {
            intent = Intent(this, AlmacenesActivity::class.java)
            startActivity(intent)
        }

        cardClientes.setOnClickListener {
            intent = Intent(this, ListarClientesActivity::class.java)
            startActivity(intent)
        }
    }
}
