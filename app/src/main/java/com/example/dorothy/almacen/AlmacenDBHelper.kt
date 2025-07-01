package com.example.dorothy.almacen

import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class AlmacenDBHelper {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("almacenes")

    fun insertarAlmacen(almacen: Almacen, onComplete: (Boolean) -> Unit) {
        val docRef = collection.document()
        val almacenConId = almacen.copy(id = docRef.id)
        docRef.set(almacenConId).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun actualizarAlmacen(almacen: Almacen, onComplete: (Boolean) -> Unit) {
        if (almacen.id.isNotEmpty()) {
            collection.document(almacen.id).set(almacen).addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
        } else {
            onComplete(false)
        }
    }

    fun eliminarAlmacen(id: String, onComplete: (Boolean) -> Unit) {
        collection.document(id).delete().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun escucharAlmacenes(onDataChange: (List<Almacen>) -> Unit) {
        collection.addSnapshotListener { value, error ->
            if (error != null) {
                onDataChange(emptyList())
                return@addSnapshotListener
            }
            val almacenes = mutableListOf<Almacen>()
            value?.documents?.forEach { doc ->
                val almacen = doc.toObject(Almacen::class.java)
                if (almacen != null) {
                    almacenes.add(almacen)
                }
            }
            onDataChange(almacenes)
        }
    }
}