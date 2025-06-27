package com.example.dorothy.almacen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dorothy.databinding.ItemAlmacenBinding

class AlmacenAdapter(
    private var almacenes: List<Almacen>,
    private val onEditClick: (Almacen) -> Unit,
    private val onDeleteClick: (Almacen) -> Unit
) : RecyclerView.Adapter<AlmacenAdapter.AlmacenViewHolder>() {

    inner class AlmacenViewHolder(val binding: ItemAlmacenBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlmacenViewHolder {
        val binding = ItemAlmacenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlmacenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlmacenViewHolder, position: Int) {
        val almacen = almacenes[position]
        with(holder.binding) {
            tvNombre.text = almacen.nombre
            tvTamano.text = "Tamaño: ${almacen.tamano}"
            tvCapacidad.text = "Capacidad Máxima: ${almacen.capacidadMaxima}"
            tvUbicacion.text = "Ubicación: ${almacen.ubicacion}"
            tvTipo.text = "Tipo: ${almacen.tipo}"

            btnEditar.setOnClickListener { onEditClick(almacen) }
            btnEliminar.setOnClickListener { onDeleteClick(almacen) }
        }
    }

    override fun getItemCount(): Int = almacenes.size

    fun updateData(newList: List<Almacen>) {
        almacenes = newList
        notifyDataSetChanged()
    }
}