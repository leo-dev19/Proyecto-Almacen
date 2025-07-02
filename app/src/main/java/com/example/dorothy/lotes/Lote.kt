package com.example.dorothy.lotes

data class Lote(
    val id: String = "",
    val fechaRegistro: String = "",
    val producto: String = "",
    val tipo: String = "",
    val fragil: Boolean = false,
    val stock: Long = 0,
    val fechaVencimiento: String = ""
)
