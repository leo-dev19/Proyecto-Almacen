package com.example.dorothy.empleado

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
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
    private val onDataChanged: (Boolean) -> Unit
) : RecyclerView.Adapter<EmpleadoAdapter.EmpleadoViewHolder>() {
    val empleadoDBHelper = EmpleadoDBHelper()

    class EmpleadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.lblNombre)
        val codigo: TextView = itemView.findViewById(R.id.lblCodigo)
        val rol: TextView = itemView.findViewById(R.id.lblRol)
        val btnModificar : Button = itemView.findViewById(R.id.btnModificarEmpleadoItem)
        val btnEliminar : Button = itemView.findViewById(R.id.btnEliminarEmpleadoItem)
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
        holder.btnEliminar.setOnClickListener {
            dialogoEliminarEmpleado(empleado)
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

        val passwordLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val inputContrasenia = TextInputEditText(context).apply {
            setText(empleado.contrasenia)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val toggleBtn = Button(context).apply {
            text = "👁"
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }

        var oculto = true
        toggleBtn.setOnClickListener {
            oculto = !oculto
            inputContrasenia.inputType = if (oculto)
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            else
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

            inputContrasenia.setSelection(inputContrasenia.text?.length ?: 0)
        }

        passwordLayout.addView(inputContrasenia)
        passwordLayout.addView(toggleBtn)

        layout.addView(inputNombre)
        layout.addView(inputApellido)
        layout.addView(inputRol)
        layout.addView(passwordLayout)

        AlertDialog.Builder(context)
            .setTitle("Modificacion datos de Empleado")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                empleado.nombre = inputNombre.text.toString()
                empleado.apellido = inputApellido.text.toString()
                empleado.rol = inputRol.text.toString()
                empleado.contrasenia = inputContrasenia.text.toString()

                empleadoDBHelper.actualizarEmpleado(empleado){ exito, mensaje ->
                    onDataChanged(exito)
                }
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }

    fun dialogoEliminarEmpleado(empleado: Empleado) {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        val inputNombre = TextView(context).apply { setText(empleado.nombre) }
        val inputApellido = TextView(context).apply { setText(empleado.apellido) }
        val inputRol = TextView(context).apply { setText(empleado.rol) }

        layout.addView(inputNombre)
        layout.addView(inputApellido)
        layout.addView(inputRol)

        AlertDialog.Builder(context)
            .setTitle("Eliminando a un Empleado")
            .setView(layout)
            .setPositiveButton("Eliminar definitivamente") { _, _ ->
                empleado.nombre = inputNombre.text.toString()
                empleado.apellido = inputApellido.text.toString()
                empleado.rol = inputRol.text.toString()

                empleadoDBHelper.eliminarEmpleado(empleado.codigo){ exito, mensaje ->
                    onDataChanged(exito)
                }
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }
}
