package com.example.dorothy.lotes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.dorothy.R

class LoteAdapter(
    private val context: Context,
    listaLotes: List<Lote>,
    private val onEditarClick: (Lote) -> Unit,
    private val onEliminarClick: (Lote) -> Unit
) : BaseAdapter() {
    private val listaLotes: MutableList<Lote> = listaLotes.toMutableList()

    override fun getCount(): Int = listaLotes.size
    override fun getItem(position: Int): Any = listaLotes[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_lote, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        val lote = listaLotes[position]

        holder.apply {
            tvIdLote.text = "ID: ${lote.id}"
            tvFechaRegistro.text = "Registrado: ${lote.fechaRegistro}"
            tvProducto.text = "Producto: ${lote.producto}"
            tvTipo.text = "Tipo: ${lote.tipo}"
            tvFragil.text = "Frágil: ${if (lote.fragil) "Si" else "No"}"
            tvStock.text = "Stock: ${lote.stock}"
            tvFechaVencimiento.text = "Vence: ${lote.fechaVencimiento}"

            btnEditar.setOnClickListener { onEditarClick(lote) }
            btnEliminar.setOnClickListener { onEliminarClick(lote) }
        }
        return view
    }

    fun actualizarLista(nuevaLista: List<Lote>) {
        listaLotes.clear()
        listaLotes.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    private class ViewHolder(view: View) {
        val tvIdLote: TextView = view.findViewById(R.id.tvIdLote)
        val tvFechaRegistro: TextView = view.findViewById(R.id.tvFechaRegistro)
        val tvProducto: TextView = view.findViewById(R.id.tvProducto)
        val tvTipo: TextView = view.findViewById(R.id.tvTipo)
        val tvFragil: TextView = view.findViewById(R.id.tvFragil)
        val tvStock: TextView = view.findViewById(R.id.tvStock)
        val tvFechaVencimiento: TextView = view.findViewById(R.id.tvFechaVencimiento)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }
}