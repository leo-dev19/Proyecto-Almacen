package com.example.dorothy.almacen

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dorothy.databinding.ActivityAlmacenesBinding
import com.example.dorothy.databinding.DialogAlmacenBinding

class AlmacenesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlmacenesBinding
    private lateinit var dbHelper: AlmacenDBHelper
    private lateinit var adapter: AlmacenAdapter
    private var listaAlmacenes: MutableList<Almacen> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlmacenesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = AlmacenDBHelper(this)
        listaAlmacenes = dbHelper.obtenerAlmacenes().toMutableList()
        adapter = AlmacenAdapter(listaAlmacenes,
            onEditClick = { almacen -> mostrarDialogoEditar(almacen) },
            onDeleteClick = { almacen -> eliminarAlmacen(almacen) }
        )

        binding.recyclerAlmacenes.layoutManager = LinearLayoutManager(this)
        binding.recyclerAlmacenes.adapter = adapter

        binding.btnAgregar.setOnClickListener { mostrarDialogoAgregar() }
    }

    private fun mostrarDialogoAgregar() {
        val dialogBinding = DialogAlmacenBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setTitle("Agregar Almacén")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.setOnShowListener {
            val btnGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnGuardar.setOnClickListener {
                val nombre = dialogBinding.etNombre.text.toString()
                val tamano = dialogBinding.etTamano.text.toString()
                val capacidad = dialogBinding.etCapacidad.text.toString()
                val ubicacion = dialogBinding.etUbicacion.text.toString()
                val tipo = dialogBinding.etTipo.text.toString()

                if (nombre.isEmpty() || tamano.isEmpty() || capacidad.isEmpty() || ubicacion.isEmpty() || tipo.isEmpty()) {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    dbHelper.insertarAlmacen(nombre, tamano, capacidad, ubicacion, tipo)
                    actualizarLista()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun mostrarDialogoEditar(almacen: Almacen) {
        val dialogBinding = DialogAlmacenBinding.inflate(LayoutInflater.from(this))
        dialogBinding.etNombre.setText(almacen.nombre)
        dialogBinding.etTamano.setText(almacen.tamano)
        dialogBinding.etCapacidad.setText(almacen.capacidadMaxima)
        dialogBinding.etUbicacion.setText(almacen.ubicacion)
        dialogBinding.etTipo.setText(almacen.tipo)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar Almacén")
            .setView(dialogBinding.root)
            .setPositiveButton("Actualizar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.setOnShowListener {
            val btnActualizar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnActualizar.setOnClickListener {
                val nombre = dialogBinding.etNombre.text.toString()
                val tamano = dialogBinding.etTamano.text.toString()
                val capacidad = dialogBinding.etCapacidad.text.toString()
                val ubicacion = dialogBinding.etUbicacion.text.toString()
                val tipo = dialogBinding.etTipo.text.toString()

                if (nombre.isEmpty() || tamano.isEmpty() || capacidad.isEmpty() || ubicacion.isEmpty() || tipo.isEmpty()) {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    dbHelper.actualizarAlmacen(almacen.id, nombre, tamano, capacidad, ubicacion, tipo)
                    actualizarLista()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun eliminarAlmacen(almacen: Almacen) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Estás seguro de eliminar este almacén?")
            .setPositiveButton("Sí") { _, _ ->
                dbHelper.eliminarAlmacen(almacen.id)
                actualizarLista()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun actualizarLista() {
        listaAlmacenes.clear()
        listaAlmacenes.addAll(dbHelper.obtenerAlmacenes())
        adapter.updateData(listaAlmacenes)
    }
}