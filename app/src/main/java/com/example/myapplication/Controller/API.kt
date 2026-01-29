package com.example.myapplication.Controller

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.Model.Pokemon
import org.json.JSONObject
import java.util.Locale

object API {
    @JvmStatic
    fun fetchPokemon(ctx: Context, nameOrId: String, callback: PokemonCallback) {
        val url = "https://pokeapi.co/api/v2/pokemon/" + nameOrId.lowercase(Locale.getDefault())
        val queue = Volley.newRequestQueue(ctx)

        val request = StringRequest(
            Request.Method.GET,
            url,
            { response: String? ->
                try {
                    val obj = JSONObject(response)
                    // creo el json, y obtengo las variables para almacenarlas
                    val id = obj.getInt("id")
                    //obtengo el id 
                    val name = obj.getString("name")
                    // obtengo el nombre
                    val height = obj.getInt("height")
                    // obtengo la altura
                    val weight = obj.getInt("weight")
                    // obtengo el peso
                    val imageUrl: String?
                    // creo la variable de la url de la imagen
                    if (obj.has("sprites") && obj.getJSONObject("sprites").has("front_default")) {
                        imageUrl = obj.getJSONObject("sprites").optString("front_default", null)
                        // almaceno la imagen en la variable anterior
                    } else {
                        imageUrl = null
                        // si no tiene imagen, será nula
                    }

                    // piedo la especie para obtener la descripción (flavor text) en español si existe
                    val speciesUrl =
                        "https://pokeapi.co/api/v2/pokemon-species/" + nameOrId.lowercase(
                            Locale.getDefault()
                        )
                    // url para las descripciones
                    val speciesRequest = StringRequest( //pido los string de las descripciones
                        Request.Method.GET,  // lo obtengo
                        speciesUrl,
                        { speciesResp: String? ->
                            var description = "Descripción no disponible"
                            // por defecto como no disponible
                            try {
                                val sObj = JSONObject(speciesResp)
                                // nuevo objeto json con la respuesta de la descipción
                                if (sObj.has("flavor_text_entries")) {
                                    val entries = sObj.getJSONArray("flavor_text_entries")
                                    // el strings que pasa tiene un formato flavor
                                    for (i in 0..<entries.length()) {
                                        // recorro el array de las entidades, es decir de las descripciones
                                        val entry = entries.getJSONObject(i)
                                        val lang = entry.getJSONObject("language")
                                        // filtro el elnguage y lo guardo en el objeto dentro del json
                                        if ("es" == lang.getString("name")) {
                                            // pillo por el nombre y pillo la descripción en español
                                            description = entry.getString("flavor_text")
                                                .replace("\n", " ")
                                                .replace("\u000c", " ")
                                                .trim { it <= ' ' }
                                            // descarto los caracteres no válidos por estar en flavor
                                            break
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                // mantener descripción por defecto si falla parseo
                            }
                            // almacenar los datos en el constructor
                            val pokemon = Pokemon(id, name, height, weight, imageUrl, description)
                            callback.onSuccess(pokemon)
                        },
                        { error: VolleyError? ->
                            // Si falla species request, devolvemos el pokemon sin descripción
                            val pokemon = Pokemon(
                                id,
                                name,
                                height,
                                weight,
                                imageUrl,
                                "Descripción no disponible"
                            )
                            callback.onSuccess(pokemon)
                        }
                    )

                    queue.add<String?>(speciesRequest)
                } catch (e: Exception) {
                    callback.onError("Error al procesar JSON")
                }
            },
            { error: VolleyError? -> callback.onError("No se encontró el Pokémon") }
        )

        queue.add<String?>(request)
    }

    open class PokemonCallback {
        open fun onSuccess(pokemon: Pokemon?) {}
        open fun onError(message: String?) {}
    }
} // JSONObject jObj = new JSONObject(response)
//jObj.get()
//jObj.getJSONArray("abilities").getJSONObject(0).get("name")
// queue.add(stringRequest)

