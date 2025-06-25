package com.example.dorothy.empleado

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EmpleadoDBHelper(context: Context): SQLiteOpenHelper(context, "empleadosdb.db", null, 3) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
        CREATE TABLE empleados (
        codigo INTEGER PRIMARY KEY AUTOINCREMENT,
        nombre TEXT,
        apellido REAL,
        rol INTEGER,
        contrasenia TEXT
        )
        """
        db.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS empleados")
        onCreate(db)
    }

    fun insertarEmpleado(empleado: Empleado): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", empleado.nombre)
            put("apellido", empleado.apellido)
            put("rol", empleado.rol)
            put("contrasenia", empleado.contrasenia)
        }
        return db.insert("empleados", null, values)
    }
    fun obtenerEmpleados(nombre: String?): List<Empleado> {
        val lista = mutableListOf<Empleado>()
        try{
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT * FROM empleados", null)
            while (cursor.moveToNext()) {
                val empleado = Empleado(
                    codigo = cursor.getInt(0),
                    nombre = cursor.getString(1),
                    apellido = cursor.getString(2),
                    rol = cursor.getString(3),
                    contrasenia = cursor.getString(4)
                )
                if(nombre == null){
                    lista.add(empleado)
                }
                if(nombre != null && cursor.getString(1).contains(nombre)){
                    lista.add(empleado)
                }
            }
            cursor.close()
        }catch (ex: Exception){
            val empleado = Empleado(0, "Error de servidor", "", ""+ex.message.toString(), "Prueba de nuevo")
            lista.add(empleado)
            return lista
        }
        return lista
    }
    fun verificarEmpleado(nombre : String, contrasenia : String) : Boolean{
        if(obtenerEmpleados(null).isEmpty()) return true
        for (e in obtenerEmpleados(null)){
            if(e.nombre == nombre && e.contrasenia == contrasenia) {
                return true
            }
        }
        return false
    }
    fun actualizarEmpleado(empleado: Empleado): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", empleado.nombre)
            put("apellido", empleado.apellido)
            put("rol", empleado.rol)
            put("contrasenia", empleado.contrasenia)
        }
        return db.update("empleados", values, "codigo=?", arrayOf(empleado.codigo.toString()))
    }
    fun eliminarEmpleado(codigo: Int): Int {
        val db = writableDatabase
        return db.delete("empleados", "codigo=?", arrayOf(codigo.toString()))
    }
}