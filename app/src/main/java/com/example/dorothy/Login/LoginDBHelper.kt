package com.example.dorothy.Login

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LoginDBHelper(context: Context) : SQLiteOpenHelper(context, "LoginDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE,
                password TEXT
            )
        """.trimIndent()
        db?.execSQL(createTable)

        val values = ContentValues().apply {
            put("username", "admin")
            put("password", "1234")
        }
        db?.insert("usuarios", null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    fun validarUsuario(username: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM usuarios WHERE username = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }
}
