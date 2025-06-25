package com.example.dorothy.empleado

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dorothy.R
import com.google.android.material.textfield.TextInputEditText

class EmpleadoAdapter(
    private val context: Context,
    private val lista: List<Empleado>,
    private val onDataChanged: () -> Unit
) : RecyclerView.Adapter<EmpleadoAdapter.EmpleadoViewHolder>() {
    val empleadoDBHelper = EmpleadoDBHelper(context)

    class EmpleadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.lblNombre)
        val codigo: TextView = itemView.findViewById(R.id.lblCodigo)
        val rol: TextView = itemView.findViewById(R.id.lblRol)
        val btnModificar : Button = itemView.findViewById(R.id.btnModificar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpleadoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_empleado_item, parent, false)
        return EmpleadoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmpleadoViewHolder, position: Int) {
        val empleado = lista[position]
        holder.nombre.text = "${empleado.nombre} ${empleado.apellido}"
        holder.codigo.text = "Codigo: " + empleado.codigo
        holder.rol.text = empleado.rol
        holder.btnModificar.setOnClickListener {
            dialogoModificarEmpleado(empleado)
        }
    }
    override fun getItemCount(): Int = lista.size

    fun dialogoModificarEmpleado(empleado: Empleado) {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        val inputNombre = EditText(context).apply { setText(empleado.nombre) }
        val inputApellido = EditText(context).apply { setText(empleado.apellido) }
        val inputRol = EditText(context).apply { setText(empleado.rol) }
        val inputContrasenia = TextInputEditText(context).apply { setText(empleado.contrasenia) }

        layout.addView(inputNombre)
        layout.addView(inputApellido)
        layout.addView(inputRol)
        layout.addView(inputContrasenia)

        AlertDialog.Builder(context)
            .setTitle("Modificacion datos de Empleado")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                empleado.nombre = inputNombre.text.toString()
                empleado.apellido = inputApellido.text.toString()
                empleado.rol = inputRol.text.toString()
                empleado.contrasenia = inputContrasenia.text.toString()

                empleadoDBHelper.actualizarEmpleado(empleado)
                onDataChanged()
            }
            .setNegativeButton("Eliminar") { _, _ ->
                empleadoDBHelper.eliminarEmpleado(empleado.codigo)
                onDataChanged()
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }


}
