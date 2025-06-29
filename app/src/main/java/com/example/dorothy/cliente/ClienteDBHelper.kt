package com.example.dorothy.cliente

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ClienteDBHelper(context: Context) : SQLiteOpenHelper(context, "clientes.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE cliente (
                codCliente INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                telefono TEXT,
                email TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS cliente")
        onCreate(db)
    }

    fun insertarCliente(cliente: Cliente): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", cliente.nombre)
            put("telefono", cliente.telefono)
            put("email", cliente.email)
        }
        return db.insert("cliente", null, values)
    }

    fun obtenerClientes(): List<Cliente> {
        val lista = mutableListOf<Cliente>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM cliente", null)

        if (cursor.moveToFirst()) {
            do {
                val cliente = Cliente(
                    codCliente = cursor.getInt(cursor.getColumnIndexOrThrow("codCliente")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                )
                lista.add(cliente)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }


    fun actualizarCliente(cliente: Cliente): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", cliente.nombre)
            put("telefono", cliente.telefono)
            put("email", cliente.email)
        }
        return db.update("cliente", values, "codCliente=?", arrayOf(cliente.codCliente.toString()))
    }

    fun eliminarCliente(codCliente: Int): Int {
        val db = writableDatabase
        return db.delete("cliente", "codCliente=?", arrayOf(codCliente.toString()))
    }
}