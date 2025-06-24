package com.example.dorothy.lotes

import android.os.Bundle
import android.widget.*
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.R

class GestionLotesActivity : AppCompatActivity() {
    private lateinit var etIdLote: EditText
    private lateinit var etFechaRegistro: EditText
    private lateinit var etIdDetalle: EditText
    private lateinit var etStock: EditText
    private lateinit var etFechaVencimiento: EditText
    private lateinit var spTipo: Spinner
    private lateinit var cbFragil: CheckBox
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var tvProductoInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gestion_lotes)

        // Referencias a los campos
        etIdLote = findViewById(R.id.etIdLote)
        etFechaRegistro = findViewById(R.id.etFechaRegistro)
        etIdDetalle = findViewById(R.id.etIdDetalle)
        etStock = findViewById(R.id.etStock)
        etFechaVencimiento = findViewById(R.id.etFechaVencimiento)
        spTipo = findViewById(R.id.spTipo)
        cbFragil = findViewById(R.id.cbFragil)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        tvProductoInfo = findViewById(R.id.tvProductoInfo)

        btnGuardar.setOnClickListener { guardarLote() }
        btnCancelar.setOnClickListener { limpiarCampos() }
    }

    private fun guardarLote() {
        val idLote = etIdLote.text.toString()
        val fecha = etFechaRegistro.text.toString()
        val tipo = spTipo.selectedItem.toString()
        val fragil = cbFragil.isChecked
        val idDetalle = etIdDetalle.text.toString()
        val stock = etStock.text.toString()
        val vencimiento = etFechaVencimiento.text.toString()

        if (idLote.isEmpty() || fecha.isEmpty() || stock.isEmpty() || idDetalle.isEmpty() || vencimiento.isEmpty()) {
            mostrarToastPersonalizado("Complete todos los campos obligatorios")
            return
        }

        // Mostrar los datos en el TextView
        val resumen = """
            ID Detalle: $idDetalle
            Stock: $stock
            Fecha de Vencimiento: $vencimiento
        """.trimIndent()

        tvProductoInfo.text = resumen

        mostrarToastPersonalizado("Lote guardado correctamente")
    }

    private fun limpiarCampos() {
        etIdLote.setText("")
        etFechaRegistro.setText("")
        etIdDetalle.setText("")
        etStock.setText("")
        etFechaVencimiento.setText("")
        spTipo.setSelection(0)
        cbFragil.isChecked = false
        tvProductoInfo.text = ""
    }

    private fun mostrarToastPersonalizado(mensaje: String) {
        val layoutInflater = layoutInflater
        val layout = layoutInflater.inflate(R.layout.mensaje_lotes, findViewById(android.R.id.content), false)

        val tvMensaje = layout.findViewById<TextView>(R.id.tvToastText)
        tvMensaje.text = mensaje

        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

}