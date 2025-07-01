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
    private lateinit var adapter: AlmacenAdapter
    private var listaAlmacenes: MutableList<Almacen> = mutableListOf()
    private val almacenHelper = AlmacenDBHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlmacenesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AlmacenAdapter(
            listaAlmacenes,
            onEditClick = { almacen -> mostrarDialogoEditar(almacen) },
            onDeleteClick = { almacen -> eliminarAlmacen(almacen) }
        )

        binding.recyclerAlmacenes.layoutManager = LinearLayoutManager(this)
        binding.recyclerAlmacenes.adapter = adapter

        binding.btnAgregar.setOnClickListener { mostrarDialogoAgregar() }

        // Actualiza la lista en tiempo real
        almacenHelper.escucharAlmacenes { almacenes ->
            listaAlmacenes.clear()
            listaAlmacenes.addAll(almacenes)
            adapter.updateData(listaAlmacenes)
        }
    }

    private fun mostrarDialogoAgregar() {
        val dialogBinding = DialogAlmacenBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setTitle("Agregar Almacén")
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()
        dialog.show()

        dialogBinding.btnGuardar.setOnClickListener {
            val nombre = dialogBinding.etNombre.text.toString()
            val tamano = dialogBinding.etTamano.text.toString()
            val capacidad = dialogBinding.etCapacidad.text.toString()
            val ubicacion = dialogBinding.etUbicacion.text.toString()
            val tipo = dialogBinding.etTipo.text.toString()

            if (nombre.isEmpty() || tamano.isEmpty() || capacidad.isEmpty() || ubicacion.isEmpty() || tipo.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val almacen = Almacen(
                    id = "",
                    nombre = nombre,
                    tamano = tamano,
                    capacidadMaxima = capacidad,
                    ubicacion = ubicacion,
                    tipo = tipo
                )
                almacenHelper.insertarAlmacen(almacen) { exito ->
                    if (exito) {
                        dialog.dismiss()
                        Toast.makeText(this, "Guardado en Firestore", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        dialogBinding.btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
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
            .setCancelable(false)
            .create()
        dialog.show()

        dialogBinding.btnGuardar.setOnClickListener {
            val nombre = dialogBinding.etNombre.text.toString()
            val tamano = dialogBinding.etTamano.text.toString()
            val capacidad = dialogBinding.etCapacidad.text.toString()
            val ubicacion = dialogBinding.etUbicacion.text.toString()
            val tipo = dialogBinding.etTipo.text.toString()

            if (nombre.isEmpty() || tamano.isEmpty() || capacidad.isEmpty() || ubicacion.isEmpty() || tipo.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val almacenEditado = almacen.copy(
                    nombre = nombre,
                    tamano = tamano,
                    capacidadMaxima = capacidad,
                    ubicacion = ubicacion,
                    tipo = tipo
                )
                almacenHelper.actualizarAlmacen(almacenEditado) { exito ->
                    if (exito) {
                        dialog.dismiss()
                        Toast.makeText(this, "Actualizado en Firestore", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        dialogBinding.btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun eliminarAlmacen(almacen: Almacen) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Estás seguro de eliminar este almacén?")
            .setPositiveButton("Sí") { _, _ ->
                almacenHelper.eliminarAlmacen(almacen.id) { exito ->
                    if (!exito) {
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
