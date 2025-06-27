package com.example.dorothy.almacen

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

class AlmacenDBHelper(context: Context) : SQLiteOpenHelper(context, "almacenes.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE almacenes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                tamano TEXT,
                capacidadMaxima TEXT,
                ubicacion TEXT,
                tipo TEXT
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS almacenes")
        onCreate(db)
    }

    fun insertarAlmacen(nombre: String, tamano: String, capacidadMaxima: String, ubicacion: String, tipo: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("tamano", tamano)
            put("capacidadMaxima", capacidadMaxima)
            put("ubicacion", ubicacion)
            put("tipo", tipo)
        }
        return db.insert("almacenes", null, values)
    }

    fun obtenerAlmacenes(): List<Almacen> {
        val almacenes = mutableListOf<Almacen>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM almacenes", null)
        if (cursor.moveToFirst()) {
            do {
                val almacen = Almacen(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    tamano = cursor.getString(cursor.getColumnIndexOrThrow("tamano")),
                    capacidadMaxima = cursor.getString(cursor.getColumnIndexOrThrow("capacidadMaxima")),
                    ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")),
                    tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
                )
                almacenes.add(almacen)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return almacenes
    }

    fun actualizarAlmacen(id: Int, nombre: String, tamano: String, capacidadMaxima: String, ubicacion: String, tipo: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("tamano", tamano)
            put("capacidadMaxima", capacidadMaxima)
            put("ubicacion", ubicacion)
            put("tipo", tipo)
        }
        return db.update("almacenes", values, "id = ?", arrayOf(id.toString()))
    }

    fun eliminarAlmacen(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("almacenes", "id = ?", arrayOf(id.toString()))
    }
}