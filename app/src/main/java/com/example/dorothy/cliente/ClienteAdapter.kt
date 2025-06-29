package com.example.myapplication



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.dorothy.R
import com.example.dorothy.cliente.Cliente
import com.example.dorothy.cliente.ClienteDBHelper


class ClienteAdapter(
    private val context: Context,
    private var listaClientes: MutableList<Cliente>,
    private val dbHelper: ClienteDBHelper
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    inner class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre = itemView.findViewById<TextView>(R.id.tvNombre)
        val tvCelular = itemView.findViewById<TextView>(R.id.tvCelular)
        val tvEmail = itemView.findViewById<TextView>(R.id.tvEmail)
        val btnEditar = itemView.findViewById<Button>(R.id.btnEditar)
        val btnEliminar = itemView.findViewById<Button>(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = listaClientes[position]
        holder.tvNombre.text = cliente.nombre
        holder.tvCelular.text = cliente.telefono
        holder.tvEmail.text = cliente.email

        holder.btnEditar.setOnClickListener {
            mostrarDialogoEditar(cliente, position)
        }

        holder.btnEliminar.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Deseas eliminar a ${cliente.nombre}?")
                .setPositiveButton("Sí") { _, _ ->
                    dbHelper.eliminarCliente(cliente.codCliente)
                    listaClientes.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, listaClientes.size)
                    Toast.makeText(context, "Cliente eliminado", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun getItemCount(): Int = listaClientes.size

    private fun mostrarDialogoEditar(cliente: Cliente, position: Int) {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 16)
        }

        val inputNombre = EditText(context).apply {
            hint = "Nombre"
            setText(cliente.nombre)
        }

        val inputCelular = EditText(context).apply {
            hint = "Celular"
            inputType = android.text.InputType.TYPE_CLASS_PHONE
            setText(cliente.telefono)
        }

        val inputEmail = EditText(context).apply {
            hint = "Email"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setText(cliente.email)
        }

        layout.addView(inputNombre)
        layout.addView(inputCelular)
        layout.addView(inputEmail)

        AlertDialog.Builder(context)
            .setTitle("Editar Cliente")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoNombre = inputNombre.text.toString().trim()
                val nuevoCelular = inputCelular.text.toString().trim()
                val nuevoEmail = inputEmail.text.toString().trim()

                if (nuevoNombre.isNotEmpty() && nuevoCelular.isNotEmpty() && nuevoEmail.isNotEmpty()) {
                    val clienteActualizado = Cliente(
                        codCliente = cliente.codCliente,
                        nombre = nuevoNombre,
                        telefono = nuevoCelular,
                        email = nuevoEmail
                    )
                    dbHelper.actualizarCliente(clienteActualizado)

                    listaClientes[position] = clienteActualizado
                    notifyItemChanged(position)

                    Toast.makeText(context, "Cliente actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }

    fun actualizarLista(nuevaLista: List<Cliente>) {
        listaClientes = nuevaLista.toMutableList()
        notifyDataSetChanged()
    }
}
