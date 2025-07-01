package com.example.dorothy.producto

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProductoDBHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "ProductosDB.db"
        const val DATABASE_VERSION = 1

        const val TABLE_PRODUCTO = "producto"
        const val COL_ID = "id"
        const val COL_NOMBRE = "nombre"
        const val COL_CATEGORIA = "categoria"
        const val COL_PRECIO = "precio"
        const val COL_STOCK = "stock"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """
            CREATE TABLE $TABLE_PRODUCTO (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT NOT NULL,
                $COL_CATEGORIA TEXT NOT NULL,
                $COL_PRECIO REAL NOT NULL,
                $COL_STOCK INTEGER NOT NULL
            )
        """.trimIndent()

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTO")
        onCreate(db)
    }

    // Insertar nuevo producto
    fun insertarProducto(producto: Producto): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOMBRE, producto.nombre)
            put(COL_CATEGORIA, producto.categoria)
            put(COL_PRECIO, producto.precio)
            put(COL_STOCK, producto.stock)
        }
        val resultado = db.insert(TABLE_PRODUCTO, null, values)
        db.close()
        return resultado
    }


    //Listar Productos

    fun obtenerProductos(): List<Producto> {
        val lista = mutableListOf<Producto>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCTO", null)

        if (cursor.moveToFirst()) {
            do {
                val producto = Producto(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)),
                    categoria = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORIA)),
                    precio = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECIO)),
                    stock = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STOCK))
                )
                lista.add(producto)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    //Buscar Producto por su nombre

    fun buscarPorNombre(nombre: String): List<Producto> {
        val lista = mutableListOf<Producto>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_PRODUCTO WHERE $COL_NOMBRE LIKE ?",
            arrayOf("%$nombre%")
        )

        if (cursor.moveToFirst()) {
            do {
                val producto = Producto(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)),
                    categoria = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORIA)),
                    precio = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECIO)),
                    stock = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STOCK))
                )
                lista.add(producto)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    //Buscar producto por ID

    fun obtenerProductoPorId(id: Int): Producto? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_PRODUCTO WHERE $COL_ID = ?",
            arrayOf(id.toString())
        )

        var producto: Producto? = null
        if (cursor.moveToFirst()) {
            producto = Producto(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)),
                categoria = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORIA)),
                precio = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECIO)),
                stock = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STOCK))
            )
        }

        cursor.close()
        db.close()
        return producto
    }

    //Eliminar producto

    fun eliminarProducto(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_PRODUCTO, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    //Actualizar Producto

    fun actualizarProducto(producto: Producto): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOMBRE, producto.nombre)
            put(COL_CATEGORIA, producto.categoria)
            put(COL_PRECIO, producto.precio)
            put(COL_STOCK, producto.stock)
        }
        val result = db.update(
            TABLE_PRODUCTO,
            values,
            "$COL_ID = ?",
            arrayOf(producto.id.toString())
        )
        db.close()
        return result > 0
    }




}
