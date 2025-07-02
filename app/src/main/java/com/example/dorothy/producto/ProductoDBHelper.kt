package com.example.dorothy.producto

import com.google.firebase.firestore.FirebaseFirestore

class ProductoDBHelper {
    private val db = FirebaseFirestore.getInstance()
    private val collectionName = "productos"

    fun insertarProducto(producto: Producto, onResult: (Boolean) -> Unit) {
        val newDoc = db.collection(collectionName).document()
        producto.id = newDoc.id
        newDoc.set(producto)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun obtenerProductos(onResult: (List<Producto>) -> Unit) {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { result ->
                val lista = mutableListOf<Producto>()
                for (document in result) {
                    val producto = document.toObject(Producto::class.java)
                    lista.add(producto)
                }
                onResult(lista)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun actualizarProducto(producto: Producto, onResult: (Boolean) -> Unit) {
        producto.id?.let { id ->
            db.collection(collectionName).document(id)
                .set(producto)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } ?: onResult(false)
    }

    fun eliminarProducto(id: String?, onResult: (Boolean) -> Unit) {
        id?.let {
            db.collection(collectionName).document(it)
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } ?: onResult(false)
    }
}
