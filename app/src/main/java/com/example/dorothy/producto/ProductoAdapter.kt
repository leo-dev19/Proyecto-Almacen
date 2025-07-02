package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.dorothy.R
import com.example.dorothy.producto.Producto
import com.example.dorothy.producto.ProductoDBHelper

class ProductoAdapter(
    private val context: Context,
    private var listaProductos: MutableList<Producto>,
    private val dbHelper: ProductoDBHelper
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCategoria: TextView = itemView.findViewById(R.id.tvCategoria)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val tvStock: TextView = itemView.findViewById(R.id.tvStock)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = listaProductos[position]
        holder.tvNombre.text = producto.nombre
        holder.tvCategoria.text = producto.categoria
        holder.tvPrecio.text = "S/. ${producto.precio}"
        holder.tvStock.text = "Stock: ${producto.stock}"

        holder.btnEditar.setOnClickListener {
            mostrarDialogoEditar(producto, position)
        }

        holder.btnEliminar.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Eliminar producto")
                .setMessage("¿Deseas eliminar el producto '${producto.nombre}'?")
                .setPositiveButton("Sí") { _, _ ->
                    producto.id?.let { idProducto ->
                        dbHelper.eliminarProducto(idProducto) { success ->
                            if (success) {
                                listaProductos.removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, listaProductos.size)
                                Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun getItemCount(): Int = listaProductos.size

    private fun mostrarDialogoEditar(producto: Producto, position: Int) {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 30, 40, 10)
        }

        val inputNombre = EditText(context).apply {
            hint = "Nombre"
            setText(producto.nombre)
        }

        val inputCategoria = EditText(context).apply {
            hint = "Categoría"
            setText(producto.categoria)
        }

        val inputPrecio = EditText(context).apply {
            hint = "Precio"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(producto.precio)
        }

        val inputStock = EditText(context).apply {
            hint = "Stock"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            setText(producto.stock)
        }

        layout.addView(inputNombre)
        layout.addView(inputCategoria)
        layout.addView(inputPrecio)
        layout.addView(inputStock)

        AlertDialog.Builder(context)
            .setTitle("Editar Producto")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = inputNombre.text.toString().trim()
                val categoria = inputCategoria.text.toString().trim()
                val precio = inputPrecio.text.toString().trim()
                val stock = inputStock.text.toString().trim()

                if (nombre.isNotEmpty() && categoria.isNotEmpty() && precio.isNotEmpty() && stock.isNotEmpty()) {
                    val productoActualizado = Producto(
                        id = producto.id,
                        nombre = nombre,
                        categoria = categoria,
                        precio = precio,
                        stock = stock
                    )

                    dbHelper.actualizarProducto(productoActualizado) { success ->
                        if (success) {
                            listaProductos[position] = productoActualizado
                            notifyItemChanged(position)
                            Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                }
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }

    fun actualizarLista(nuevaLista: List<Producto>) {
        listaProductos = nuevaLista.toMutableList()
        notifyDataSetChanged()
    }
}
