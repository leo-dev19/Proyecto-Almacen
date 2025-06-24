package com.example.dorothy.producto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dorothy.R


class ProductoAdapter(
    private val productos: MutableList<Producto>,
    private val onEditarClick: (Producto) -> Unit,
    private val onEliminarClick: (Producto, Int) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCategoria: TextView = itemView.findViewById(R.id.tvCategoria)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val tvStock: TextView = itemView.findViewById(R.id.tvStock)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.tvNombre.text = producto.nombre
        holder.tvCategoria.text = producto.categoria
        holder.tvPrecio.text = "S/ ${producto.precio}"
        holder.tvStock.text = "Stock: ${producto.stock}"

        holder.btnEditar.setOnClickListener { onEditarClick(producto) }
        holder.btnEliminar.setOnClickListener { onEliminarClick(producto, position) }
    }

    override fun getItemCount() = productos.size

    fun eliminarProducto(posicion: Int) {
        productos.removeAt(posicion)
        notifyItemRemoved(posicion)
    }
}
