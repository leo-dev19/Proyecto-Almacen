package com.example.dorothy.cliente


import com.google.firebase.firestore.FirebaseFirestore

class ClienteDBHelper {
    private val db = FirebaseFirestore.getInstance()
    private val collectionName = "clientes"

    fun insertarCliente(cliente: Cliente, onResult: (Boolean) -> Unit) {
        val newDoc = db.collection(collectionName).document()
        cliente.codCliente = newDoc.id
        newDoc.set(cliente)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun obtenerClientes(onResult: (List<Cliente>) -> Unit) {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { result ->
                val lista = mutableListOf<Cliente>()
                for (document in result) {
                    val cliente = document.toObject(Cliente::class.java)
                    lista.add(cliente)
                }
                onResult(lista)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun actualizarCliente(cliente: Cliente, onResult: (Boolean) -> Unit) {
        cliente.codCliente?.let { id ->
            db.collection(collectionName).document(id)
                .set(cliente)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } ?: onResult(false)
    }

    fun eliminarCliente(codCliente: String?, onResult: (Boolean) -> Unit) {
        codCliente?.let { id ->
            db.collection(collectionName).document(id)
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } ?: onResult(false)
    }
}