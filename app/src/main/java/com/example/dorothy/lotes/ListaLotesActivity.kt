package com.example.dorothy.lotes

import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.R
import com.google.firebase.firestore.FirebaseFirestore
import android.text.Editable
import android.text.TextWatcher

class ListaLotesActivity : AppCompatActivity() {

    // Vistas
    private lateinit var listView: ListView
    private lateinit var etBuscar: EditText
    private lateinit var btnRegresar: Button

    // Firebase
    private lateinit var db: FirebaseFirestore

    // Datos
    private lateinit var loteList: ArrayList<Lote>
    private lateinit var adapter: LoteAdapter
    private val listaProductos = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lotes_registrados)

        // Referencias
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

        // Búsqueda
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val texto = s.toString().lowercase()
                val filtrados = loteList.filter { lote ->
                    lote.id.lowercase().contains(texto) ||
                            lote.producto.lowercase().contains(texto) ||
                            lote.tipo.lowercase().contains(texto)
                }
                adapter = LoteAdapter(
                    this@ListaLotesActivity,
                    ArrayList(filtrados),
                    onEditarClick = { dialogoEditar(it) },
                    onEliminarClick = { eliminarLote(it) }
                )
                listView.adapter = adapter
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Obtener lotes desde Firebase
    private fun obtenerLotes() {
        db.collection("Lotes")
            .get()
            .addOnSuccessListener { result ->
                loteList.clear()
                for (document in result) {
                    val lote = Lote(
                        id = document.getString("idLote") ?: "",
                        fechaRegistro = document.getString("fechaRegistro") ?: "",
                        producto = document.getString("producto") ?: "",
                        tipo = document.getString("tipo") ?: "",
                        fragil = document.getBoolean("fragil") ?: false,
                        stock = document.getString("stock") ?: "0",
                        fechaVencimiento = document.getString("fechaVencimiento") ?: ""
                    )
                    loteList.add(lote)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                mostrarToast("Error al cargar datos")
            }
    }

    // Cargar productos desde Firebase
    private fun cargarProductosParaEditar(onComplete: (ArrayList<String>) -> Unit) {
        db.collection("Productos")
            .get()
            .addOnSuccessListener { result ->
                val productos = ArrayList<String>()
                productos.add("Seleccione un producto")
                for (document in result) {
                    val nombre = document.getString("nombre") ?: "Sin nombre"
                    productos.add(nombre)
                }
                onComplete(productos)
            }
            .addOnFailureListener {
                mostrarToast("Error al cargar productos")
                onComplete(arrayListOf())
            }
    }

    // Editar lote
    private fun dialogoEditar(lote: Lote) {
        val dialogView = layoutInflater.inflate(R.layout.editar_lote, null)

        val spProducto = dialogView.findViewById<Spinner>(R.id.spProductoEditar)
        val etStock = dialogView.findViewById<EditText>(R.id.etStockEditar)
        val spTipo = dialogView.findViewById<Spinner>(R.id.spTipoEditar)
        val cbFragil = dialogView.findViewById<CheckBox>(R.id.cbFragilEditar)
        val etFechaVenc = dialogView.findViewById<EditText>(R.id.etFechaVencimientoEditar)

        // Cargar datos actuales
        etStock.setText(lote.stock)
        cbFragil.isChecked = lote.fragil
        etFechaVenc.setText(lote.fechaVencimiento)

        // Spinner tipo
        val adapterTipo = ArrayAdapter.createFromResource(
            this,
            R.array.tipo_lote,
            android.R.layout.simple_spinner_item
        )
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipo.adapter = adapterTipo
        val tipoIndex = adapterTipo.getPosition(lote.tipo)
        spTipo.setSelection(if (tipoIndex != -1) tipoIndex else 0)

        // Spinner producto desde Firebase
        cargarProductosParaEditar { productos ->
            val adapterProducto = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                productos
            )
            adapterProducto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spProducto.adapter = adapterProducto

            val productoIndex = productos.indexOf(lote.producto)
            spProducto.setSelection(if (productoIndex != -1) productoIndex else 0)
        }

        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnActualizar).setOnClickListener {
            val nuevoStock = etStock.text.toString().trim()
            val nuevoTipo = spTipo.selectedItem.toString()
            val nuevoProducto = spProducto.selectedItem.toString()
            val esFragil = cbFragil.isChecked
            val nuevaFechaVenc = etFechaVenc.text.toString().trim()

            if (nuevoStock.isEmpty() || nuevaFechaVenc.isEmpty() || nuevoProducto == "Seleccione un producto") {
                mostrarToast("Complete todos los campos")
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

    // Eliminar lote
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