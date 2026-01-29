package com.example.myapplication.Controller

import java.util.Locale

object LoginController {
    @JvmStatic
    fun cifrarCesar(texto: String): String {
        var texto = texto
        val alfabeto = "abcdefghijklmnopqrstuvwxyz"
        var resultado = ""
        texto = texto.lowercase(Locale.getDefault())

        for (i in 0..<texto.length) {
            val c = texto.get(i)
            val pos = alfabeto.indexOf(c)
            if (pos != -1) {
                val nuevo = alfabeto.get((pos + 1) % 26)
                resultado = resultado + nuevo
            } else {
                resultado = resultado + c
            }
        }
        return resultado
    }
}
