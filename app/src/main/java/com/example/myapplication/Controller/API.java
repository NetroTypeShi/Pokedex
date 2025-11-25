package com.example.myapplication.Controller;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.myapplication.Model.Pokemon;

public class API {


    public static class PokemonCallback {
        public void onSuccess(Pokemon pokemon) {}
        public void onError(String message) {}
    }

    public static void fetchPokemon(Context ctx, String nameOrId, PokemonCallback callback) {
        String url = "https://pokeapi.co/api/v2/pokemon/" + nameOrId.toLowerCase();
        RequestQueue queue = Volley.newRequestQueue(ctx);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        // creo el json, y obtengo las variables para almacenarlas
                         int id = obj.getInt("id");
                        //obtengo el id 
                         String name = obj.getString("name");
                        // obtengo el nombre
                         int height = obj.getInt("height");
                        // obtengo la altura
                         int weight = obj.getInt("weight");
                        // obtengo el peso
                        String imageUrl;
                        // creo la variable de la url de la imagen
                        if (obj.has("sprites") && obj.getJSONObject("sprites").has("front_default")) {
                            imageUrl = obj.getJSONObject("sprites").optString("front_default", null);
                            // almaceno la imagen en la variable anterior
                        } else {
                            imageUrl = null;
                            // si no tiene imagen, será nula
                        }

                        // piedo la especie para obtener la descripción (flavor text) en español si existe
                        String speciesUrl = "https://pokeapi.co/api/v2/pokemon-species/" + nameOrId.toLowerCase();
                        // url para las descripciones
                        StringRequest speciesRequest = new StringRequest(
                            //pido los string de las descripciones
                                Request.Method.GET,
                            // lo obtengo
                                speciesUrl,
                                speciesResp -> {
                                    String description = "Descripción no disponible";
                                    // por defecto como no disponible
                                    try {
                                        JSONObject sObj = new JSONObject(speciesResp);
                                        // nuevo objeto json con la respuesta de la descipción
                                        if (sObj.has("flavor_text_entries")) {
                                            JSONArray entries = sObj.getJSONArray("flavor_text_entries");
                                            // el strings que pasa tiene un formato flavor
                                            for (int i = 0; i < entries.length(); i++) {
                                                // recorro el array de las entidades, es decir de las descripciones
                                                JSONObject entry = entries.getJSONObject(i);
                                                JSONObject lang = entry.getJSONObject("language");
                                                // filtro el elnguage y lo guardo en el objeto dentro del json
                                                if ("es".equals(lang.getString("name"))) {
                                                    // pillo por el nombre y pillo la descripción en español
                                                    description = entry.getString("flavor_text")
                                                            .replace("\n", " ")
                                                            .replace("\f", " ")
                                                            .trim();
                                                    // descarto los caracteres no válidos por estar en flavor
                                                    break;
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        // mantener descripción por defecto si falla parseo
                                    }
                                    // almacenamos los datos en el constructor
                                    Pokemon pokemon = new Pokemon(id, name, height, weight, imageUrl, description);
                                    callback.onSuccess(pokemon);
                                },
                                error -> {
                                    // Si falla species request, devolvemos el pokemon sin descripción
                                    Pokemon pokemon = new Pokemon(id, name, height, weight, imageUrl, "Descripción no disponible");
                                    callback.onSuccess(pokemon);
                                }
                        );

                        queue.add(speciesRequest);

                    } catch (Exception e) {
                        callback.onError("Error al procesar JSON");
                    }
                },
                error -> callback.onError("No se encontró el Pokémon")
        );

        queue.add(request);
    }
}

// JSONObject jObj = new JSONObject(response)
//jObj.get()
//jObj.getJSONArray("abilities").getJSONObject(0).get("name")
// queue.add(stringRequest)
