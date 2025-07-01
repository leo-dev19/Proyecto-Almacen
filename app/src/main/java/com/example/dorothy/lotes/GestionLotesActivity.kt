package com.example.dorothy.lotes

import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.R
import com.google.firebase.firestore.FirebaseFirestore

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
    private lateinit var listViewLotes: ListView

    private lateinit var db: FirebaseFirestore
    private lateinit var loteList: ArrayList<Lote>
    private lateinit var adapter: LoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gestion_lotes)

        // 🔥 Firebase
        db = FirebaseFirestore.getInstance()

        // 🧠 Referencias
        etIdLote = findViewById(R.id.etIdLote)
        etFechaRegistro = findViewById(R.id.etFechaRegistro)
        etIdDetalle = findViewById(R.id.etIdDetalle)
        etStock = findViewById(R.id.etStock)
        etFechaVencimiento = findViewById(R.id.etFechaVencimiento)
        spTipo = findViewById(R.id.spTipo)
        cbFragil = findViewById(R.id.cbFragil)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        listViewLotes = findViewById(R.id.listViewLotes)

        loteList = ArrayList()

        adapter = LoteAdapter(this, loteList,
            onEditarClick = { lote -> cargarDatosLote(lote) },
            onEliminarClick = { lote -> eliminarLote(lote) }
        )

        listViewLotes.adapter = adapter

        btnGuardar.setOnClickListener { guardarOActualizarLote() }
        btnCancelar.setOnClickListener { limpiarCampos() }

        obtenerLotes()
    }

    // ✅ Crear o Editar
    private fun guardarOActualizarLote() {
        val idLote = etIdLote.text.toString().trim()
        val fecha = etFechaRegistro.text.toString().trim()
        val tipo = spTipo.selectedItem.toString()
        val fragil = cbFragil.isChecked
        val idDetalle = etIdDetalle.text.toString().trim()
        val stock = etStock.text.toString().trim()
        val vencimiento = etFechaVencimiento.text.toString().trim()

        if (idLote.isEmpty() || fecha.isEmpty() || stock.isEmpty() || idDetalle.isEmpty() || vencimiento.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        val lote = hashMapOf(
            "fechaRegistro" to fecha,
            "tipo" to tipo,
            "fragil" to fragil,
            "idDetalle" to idDetalle,
            "stock" to stock,
            "fechaVencimiento" to vencimiento
        )

        db.collection("Lotes").document(idLote)
            .set(lote)
            .addOnSuccessListener {
                mostrarToast("Lote guardado/actualizado")
                limpiarCampos()
                obtenerLotes()
            }
            .addOnFailureListener {
                mostrarToast("Error al guardar")
            }
    }

    // 🔍 Leer lotes
    private fun obtenerLotes() {
        db.collection("Lotes")
            .get()
            .addOnSuccessListener { result ->
                loteList.clear()
                for (doc in result) {
                    val lote = Lote(
                        id = doc.id,
                        fechaRegistro = doc.getString("fechaRegistro") ?: "",
                        tipo = doc.getString("tipo") ?: "",
                        fragil = doc.getBoolean("fragil") ?: false,
                        idDetalle = doc.getString("idDetalle") ?: "",
                        stock = doc.getString("stock") ?: "0",
                        fechaVencimiento = doc.getString("fechaVencimiento") ?: ""
                    )
                    loteList.add(lote)
                }
                adapter.notifyDataSetChanged()
            }
    }

    // ✍️ Cargar datos para Editar
    private fun cargarDatosLote(lote: Lote) {
        etIdLote.setText(lote.id)
        etFechaRegistro.setText(lote.fechaRegistro)
        etIdDetalle.setText(lote.idDetalle)
        etStock.setText(lote.stock)
        etFechaVencimiento.setText(lote.fechaVencimiento)

        val tipoPos = (spTipo.adapter as ArrayAdapter<String>).getPosition(lote.tipo)
        spTipo.setSelection(tipoPos)

        cbFragil.isChecked = lote.fragil
    }

    // ❌ Eliminar
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

    // 🔄 Limpiar campos
    private fun limpiarCampos() {
        etIdLote.setText("")
        etFechaRegistro.setText("")
        etIdDetalle.setText("")
        etStock.setText("")
        etFechaVencimiento.setText("")
        spTipo.setSelection(0)
        cbFragil.isChecked = false
    }

    // 🔔 Toast personalizado
    private fun mostrarToast(mensaje: String) {
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
