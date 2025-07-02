package com.example.dorothy.lotes

import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.R
import com.google.firebase.firestore.FirebaseFirestore
import android.text.Editable
import android.text.TextWatcher
import java.util.Calendar
import android.app.DatePickerDialog

class ListaLotesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var etBuscar: EditText
    private lateinit var btnRegresar: Button

    private lateinit var db: FirebaseFirestore
    private lateinit var loteList: ArrayList<Lote>
    private lateinit var adapter: LoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lotes_registrados)

        listView = findViewById(R.id.listViewLotesRegistrados)
        etBuscar = findViewById(R.id.etBuscar)
        btnRegresar = findViewById(R.id.btnRegresar)

        db = FirebaseFirestore.getInstance()
        loteList = ArrayList()

        adapter = LoteAdapter(
            this,
            loteList,
            onEditarClick = { dialogoEditar(it) },
            onEliminarClick = { eliminarLote(it) }
        )

        listView.adapter = adapter

        obtenerLotes()

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarLotes(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnRegresar.setOnClickListener { finish() }
    }

    private fun filtrarLotes(texto: String) {
        val textoFiltrado = texto.lowercase()
        val listaFiltrada = loteList.filter { lote ->
            lote.id.lowercase().contains(textoFiltrado) ||
                    lote.producto.lowercase().contains(textoFiltrado) ||
                    lote.tipo.lowercase().contains(textoFiltrado) ||
                    lote.fechaVencimiento.lowercase().contains(textoFiltrado)
        }
        adapter.actualizarLista(listaFiltrada)
    }

    private fun obtenerLotes() {
        db.collection("Lotes")
            .get()
            .addOnSuccessListener { result ->
                loteList.clear()
                for (document in result) {
                    val lote = Lote(
                        id = document.id,
                        fechaRegistro = document.getString("fechaRegistro") ?: "",
                        producto = document.getString("producto") ?: "",
                        tipo = document.getString("tipo") ?: "",
                        fragil = document.getBoolean("fragil") ?: false,
                        stock = document.getLong("stock") ?: 0,
                        fechaVencimiento = document.getString("fechaVencimiento") ?: ""
                    )
                    loteList.add(lote)
                }
                adapter.actualizarLista(loteList)
            }
            .addOnFailureListener {
                mostrarToast("Error al cargar datos")
            }
    }

    private fun dialogoEditar(lote: Lote) {
        val dialogView = layoutInflater.inflate(R.layout.editar_lote, null)

        val spProducto = dialogView.findViewById<Spinner>(R.id.spProductoEditar)
        val etStock = dialogView.findViewById<EditText>(R.id.etStockEditar)
        val spTipo = dialogView.findViewById<Spinner>(R.id.spTipoEditar)
        val cbFragil = dialogView.findViewById<CheckBox>(R.id.cbFragilEditar)
        val etFechaVenc = dialogView.findViewById<EditText>(R.id.etFechaVencimientoEditar)

        etStock.setText(lote.stock.toString())
        cbFragil.isChecked = lote.fragil
        etFechaVenc.setText(lote.fechaVencimiento)

        etFechaVenc.setOnClickListener {
            val calendar = Calendar.getInstance()

            // Si ya hay una fecha escrita, la usamos como valor inicial
            val dateParts = etFechaVenc.text.toString().split("/")
            if (dateParts.size == 3) {
                calendar.set(Calendar.DAY_OF_MONTH, dateParts[0].toInt())
                calendar.set(Calendar.MONTH, dateParts[1].toInt() - 1)
                calendar.set(Calendar.YEAR, dateParts[2].toInt())
            }

            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                etFechaVenc.setText(fechaSeleccionada)
            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.datePicker.minDate = System.currentTimeMillis()

            datePicker.show()
        }

        val adapterTipo = ArrayAdapter.createFromResource(
            this,
            R.array.tipo_lote,
            android.R.layout.simple_spinner_item
        )
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipo.adapter = adapterTipo
        spTipo.setSelection(adapterTipo.getPosition(lote.tipo))

        val productos = loteList.map { it.producto }.distinct()
        val adapterProducto = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            productos
        )
        adapterProducto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProducto.adapter = adapterProducto
        spProducto.setSelection(productos.indexOf(lote.producto))

        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnActualizar).setOnClickListener {
            val nuevoStock = etStock.text.toString().trim().toLongOrNull()
            val nuevoTipo = spTipo.selectedItem.toString()
            val nuevoProducto = spProducto.selectedItem.toString()
            val esFragil = cbFragil.isChecked
            val nuevaFechaVenc = etFechaVenc.text.toString().trim()

            if (nuevoStock == null || nuevaFechaVenc.isEmpty() || nuevoProducto.isEmpty()) {
                mostrarToast("Complete todos los campos correctamente")
                return@setOnClickListener
            }

            val datosActualizados = mapOf(
                "producto" to nuevoProducto,
                "tipo" to nuevoTipo,
                "fragil" to esFragil,
                "stock" to nuevoStock,
                "fechaVencimiento" to nuevaFechaVenc
            )

            db.collection("Lotes").document(lote.id)
                .update(datosActualizados)
                .addOnSuccessListener {
                    mostrarToast("Lote actualizado")
                    obtenerLotes()
                    dialog.dismiss()
                }
                .addOnFailureListener {
                    mostrarToast("Error al actualizar")
                }
        }
        dialogView.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun eliminarLote(lote: Lote) {
        db.collection("Lotes").document(lote.id)
            .delete()
            .addOnSuccessListener {
                mostrarToast("Lote eliminado")
                obtenerLotes()
            }
            .addOnFailureListener {
                mostrarToast("Error al eliminar")
            }
    }

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