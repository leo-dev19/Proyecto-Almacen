package com.example.dorothy.producto

data class Producto(
    val id: Int = 0,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val stock: Int
)
