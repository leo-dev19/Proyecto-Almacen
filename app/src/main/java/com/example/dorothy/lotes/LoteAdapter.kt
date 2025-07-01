package com.example.dorothy.lotes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.dorothy.R

class LoteAdapter(
    private val context: Context,
    private val listaLotes: ArrayList<Lote>,
    private val onEditarClick: (Lote) -> Unit,
    private val onEliminarClick: (Lote) -> Unit
) : BaseAdapter() {

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

        holder.tvIdLote.text = "ID: ${lote.id}"
        holder.tvFechaRegistro.text = "Fecha Registro: ${lote.fechaRegistro}"
        holder.tvTipo.text = "Tipo: ${lote.tipo}"
        holder.tvFragil.text = "Frágil: ${if (lote.fragil) "Sí" else "No"}"
        holder.tvStock.text = "Stock: ${lote.stock}"
        holder.tvFechaVencimiento.text = "Vence: ${lote.fechaVencimiento}"

        holder.btnEditar.setOnClickListener { onEditarClick(lote) }
        holder.btnEliminar.setOnClickListener { onEliminarClick(lote) }

        return view
    }

    private class ViewHolder(view: View) {
        val tvIdLote: TextView = view.findViewById(R.id.tvIdLote)
        val tvFechaRegistro: TextView = view.findViewById(R.id.tvFechaRegistro)
        val tvTipo: TextView = view.findViewById(R.id.tvTipo)
        val tvFragil: TextView = view.findViewById(R.id.tvFragil)
        val tvStock: TextView = view.findViewById(R.id.tvStock)
        val tvFechaVencimiento: TextView = view.findViewById(R.id.tvFechaVencimiento)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }
}