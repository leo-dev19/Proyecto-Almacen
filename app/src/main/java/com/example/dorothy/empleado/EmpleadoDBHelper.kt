package com.example.dorothy.empleado

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class EmpleadoDBHelper() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionNombre : String = "empleados"

    fun generarCodigoEmpleado(onResult: (String) -> Unit) {
        db.collection(collectionNombre)
            .orderBy("codigo", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { docs ->
                val ultimoCodigo = docs.firstOrNull()?.getString("codigo") ?: "E000"
                val nuevoNumero = ultimoCodigo.substring(1).toInt() + 1
                val nuevoCodigo = "E" + nuevoNumero.toString().padStart(3, '0')
                onResult(nuevoCodigo)
            }
            .addOnFailureListener { onResult("E001") }
    }

    fun insertarEmpleado(empleado: Empleado, onResult: (Boolean, String) -> Unit) {
        generarCodigoEmpleado { codigo ->
            val empleadoConCodigo = empleado.copy(codigo = codigo)
            db.collection(collectionNombre)
                .document(codigo)
                .set(empleadoConCodigo)
                .addOnSuccessListener {
                    onResult(true, "Nuevo Empleado "+codigo)
                }
                .addOnFailureListener { e ->
                    onResult(false, e.message ?: "Error desconocido")
                }
        }
    }
    fun obtenerEmpleados(nombre: String?, onResult: (List<Empleado>) -> Unit) {
        db.collection(collectionNombre)
            .get()
            .addOnSuccessListener { resultado ->
                val lista = resultado.documents.mapNotNull { doc ->
                    val empleado = doc.toObject(Empleado::class.java)
                    if (empleado != null) {
                        if (nombre == null || "${empleado.nombre} ${empleado.apellido}".contains(nombre, true)) {
                            empleado
                        } else null
                    } else null
                }
                onResult(lista)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun obtenerEmpleados(nombre: String?) : List<Empleado>{
        var lista : List<Empleado> = emptyList()
        obtenerEmpleados(nombre){ resultado ->
            lista = resultado
        }
        return lista
    }

    fun verificarEmpleado(nombre : String, contrasenia : String) : Boolean{
        for (e in obtenerEmpleados(null)){
            if(e.nombre == nombre && e.contrasenia == contrasenia) return true
        }
        return false
    }
    fun actualizarEmpleado(empleado: Empleado, onResult: (Boolean, String) -> Unit) {
        db.collection(collectionNombre)
            .document(empleado.codigo)
            .set(empleado)
            .addOnSuccessListener {
                onResult(true, "Empleado ${empleado.codigo} actualizado")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Error al actualizar empleado")
            }
    }
    fun eliminarEmpleado(codigo: String, onResult: (Boolean, String) -> Unit) {
        db.collection(collectionNombre)
            .document(codigo)
            .delete()
            .addOnSuccessListener {
                onResult(true, "Empleado $codigo eliminado")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Error al eliminar empleado")
            }
    }
}