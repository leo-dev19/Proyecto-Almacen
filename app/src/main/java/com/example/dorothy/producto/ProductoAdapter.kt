package com.example.dorothy.producto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.dorothy.R

class ProductoAdapter(
    private val context: Context,
    private var listaProductos: MutableList<Producto>,
    private val dbHelper: ProductoDBHelper,
    private var listaOriginal: List<Producto> = listaProductos.toList()
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
        holder.tvPrecio.text = "S/ ${producto.precio}"
        holder.tvStock.text = "Stock: ${producto.stock}"

        holder.btnEditar.setOnClickListener {
            mostrarDialogoEditar(producto, position)
        }

        holder.btnEliminar.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Deseas eliminar el producto ${producto.nombre}?")
                .setPositiveButton("Sí") { _, _ ->
                    dbHelper.eliminarProducto(producto.id)
                    listaProductos.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, listaProductos.size)
                    Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun getItemCount() = listaProductos.size

    private fun mostrarDialogoEditar(producto: Producto, position: Int) {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 16)
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
            setText(producto.precio.toString())
        }

        val inputStock = EditText(context).apply {
            hint = "Stock"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            setText(producto.stock.toString())
        }

        layout.apply {
            addView(inputNombre)
            addView(inputCategoria)
            addView(inputPrecio)
            addView(inputStock)
        }

        AlertDialog.Builder(context)
            .setTitle("Editar Producto")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoNombre = inputNombre.text.toString().trim()
                val nuevaCategoria = inputCategoria.text.toString().trim()
                val nuevoPrecio = inputPrecio.text.toString().toDoubleOrNull()
                val nuevoStock = inputStock.text.toString().toIntOrNull()

                if (nuevoNombre.isNotEmpty() && nuevaCategoria.isNotEmpty() &&
                    nuevoPrecio != null && nuevoStock != null
                ) {
                    val productoActualizado = Producto(
                        id = producto.id,
                        nombre = nuevoNombre,
                        categoria = nuevaCategoria,
                        precio = nuevoPrecio,
                        stock = nuevoStock
                    )

                    dbHelper.actualizarProducto(productoActualizado)
                    listaProductos[position] = productoActualizado
                    notifyItemChanged(position)
                    Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }

    fun actualizarLista(nuevaLista: MutableList<Producto>) {
        listaProductos.clear()
        listaProductos.addAll(nuevaLista)
        notifyDataSetChanged()
    }



}
