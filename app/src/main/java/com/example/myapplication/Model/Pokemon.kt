package com.example.myapplication.Model

class Pokemon // Nuevo constructor con description
// Constructor antiguo mantenido si lo usan en otras partes
@JvmOverloads constructor(
    @JvmField val id: Int,
    @JvmField val name: String?,
    @JvmField val height: Int,
    @JvmField val weight: Int,
    @JvmField val imageUrl: String?,
    @JvmField val description: String? = null
)
