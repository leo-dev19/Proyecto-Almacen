package com.example.dorothy.lotes

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog

class GestionLotesActivity : AppCompatActivity() {

    // Vistas
    private lateinit var spProducto: Spinner
    private lateinit var spTipo: Spinner
    private lateinit var etStock: EditText
    private lateinit var etFechaVencimiento: EditText
    private lateinit var cbFragil: CheckBox
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnVerLotes: LinearLayout

    // Firebase
    private lateinit var db: FirebaseFirestore

    // Spinner Producto (desde Firebase)
    private val listaProductos = ArrayList<String>()
    private val listaIdsProductos = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gestion_lotes)

        // Instancia Firebase
        db = FirebaseFirestore.getInstance()

        // Vistas
        spProducto = findViewById(R.id.spProducto)
        spTipo = findViewById(R.id.spTipo)
        etStock = findViewById(R.id.etStock)
        etFechaVencimiento = findViewById(R.id.etFechaVencimiento)
        cbFragil = findViewById(R.id.cbFragil)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnVerLotes = findViewById(R.id.btnVerLotes)

        // Cargar datos en los spinners
        cargarProductos() // Desde Firebase
        cargarTipoLote()  // Desde strings.xml

        // Calendario para fecha de vencicmiento
        etFechaVencimiento.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val fechaSeleccionada = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                etFechaVencimiento.setText(fechaSeleccionada)
            }, year, month, day)

            // Bloquear fechas pasadas
            datePicker.datePicker.minDate = System.currentTimeMillis()

            datePicker.show()
        }

        // Botones
        btnGuardar.setOnClickListener { guardarLote() }
        btnCancelar.setOnClickListener { limpiarCampos() }
        btnVerLotes.setOnClickListener {
            Toast.makeText(this, "Abriendo Lotes Registrados", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ListaLotesActivity::class.java))
        }
    }

    // Spinner tipo lote
    private fun cargarTipoLote() {
        ArrayAdapter.createFromResource(
            this,
            R.array.tipo_lote,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spTipo.adapter = adapter
        }
    }

    // Spinner productos desde Firebase
    private fun cargarProductos() {
        db.collection("Productos") //cambiar coleccion
            .get()
            .addOnSuccessListener { result ->
                listaProductos.clear()
                listaIdsProductos.clear()

                listaProductos.add("Seleccione un producto") // Primera opción vacía

                for (document in result) {
                    val nombre = document.getString("nombre") ?: "Sin nombre" //cambiar campo
                    val id = document.id

                    listaProductos.add(nombre)
                    listaIdsProductos.add(id)
                }
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    listaProductos
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spProducto.adapter = adapter
            }
            .addOnFailureListener {
                mostrarToast("Error al cargar productos")
            }
    }

    // Guardar lote
    private fun guardarLote() {
        val posicionProducto = spProducto.selectedItemPosition
        val posicionTipo = spTipo.selectedItemPosition

        val nombreProducto = listaProductos.getOrNull(posicionProducto) ?: ""
        val tipo = spTipo.selectedItem.toString()
        val stock = etStock.text.toString().trim().toLongOrNull()
        val fechaVenc = etFechaVencimiento.text.toString().trim()
        val fragil = cbFragil.isChecked

        // Validación
        if (posicionProducto == 0 || posicionTipo == 0 || stock == null || fechaVenc.isEmpty()) {
            mostrarToast("Complete todos los campos correctamente")
            return
        }

        val idLote = generarIdLote()
        val fechaRegistro = obtenerFechaActual()

        val lote = hashMapOf(
            "producto" to nombreProducto,
            "fechaRegistro" to fechaRegistro,
            "tipo" to tipo,
            "fragil" to fragil,
            "stock" to stock.toLong(),
            "fechaVencimiento" to fechaVenc
        )

        db.collection("Lotes").document(idLote)
            .set(lote)
            .addOnSuccessListener {
                mostrarToast("Lote guardado correctamente")
                limpiarCampos()
            }
            .addOnFailureListener {
                mostrarToast("Error al guardar")
            }
    }

    // ID automático
    private fun generarIdLote(): String {
        return "L" + System.currentTimeMillis().toString()
    }

    // Fecha actual
    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    // Limpiar
    private fun limpiarCampos() {
        spProducto.setSelection(0)
        spTipo.setSelection(0)
        etStock.text.clear()
        etFechaVencimiento.text.clear()
        cbFragil.isChecked = false
    }

    // Toast personalizado
    private fun mostrarToast(mensaje: String) {
        val layout = layoutInflater.inflate(
            R.layout.mensaje_lotes,
            findViewById(android.R.id.content),
            false
        )
        val tvMensaje = layout.findViewById<TextView>(R.id.tvToastText)
        tvMensaje.text = mensaje

        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}
