package com.example.dorothy.lotes

data class Lote(
    val id: String = "",
    val fechaRegistro: String = "",
    val tipo: String = "",
    val fragil: Boolean = false,
    val idDetalle: String = "",
    val stock: String = "0",
    val fechaVencimiento: String = ""
)
