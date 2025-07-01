package com.example.dorothy.cliente

data class Cliente(
    var codCliente: String? = null,  // Ahora es String y opcional
    var nombre: String? = null,
    var telefono: String? = null,
    var email: String? = null
)