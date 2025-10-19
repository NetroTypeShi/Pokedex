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

    public interface PokemonCallback {
        void onSuccess(Pokemon pokemon);
        void onError(String message);
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
                        final int id = obj.getInt("id");
                        final String name = obj.getString("name");
                        final int height = obj.getInt("height");
                        final int weight = obj.getInt("weight");
                        final String imageUrl;
                        if (obj.has("sprites") && obj.getJSONObject("sprites").has("front_default")) {
                            imageUrl = obj.getJSONObject("sprites").optString("front_default", null);
                        } else {
                            imageUrl = null;
                        }

                        // Ahora pedimos la especie para obtener la descripción (flavor text) en inglés
                        String speciesUrl = "https://pokeapi.co/api/v2/pokemon-species/" + nameOrId.toLowerCase();
                        StringRequest speciesRequest = new StringRequest(
                                Request.Method.GET,
                                speciesUrl,
                                speciesResp -> {
                                    String description = "Descripción no disponible";
                                    try {
                                        JSONObject sObj = new JSONObject(speciesResp);
                                        if (sObj.has("flavor_text_entries")) {
                                            JSONArray entries = sObj.getJSONArray("flavor_text_entries");
                                            for (int i = 0; i < entries.length(); i++) {
                                                JSONObject entry = entries.getJSONObject(i);
                                                JSONObject lang = entry.getJSONObject("language");
                                                if ("en".equals(lang.getString("name"))) {
                                                    description = entry.getString("flavor_text")
                                                            .replace("\n", " ")
                                                            .replace("\f", " ")
                                                            .trim();
                                                    break;
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        // mantener descripción por defecto si falla parseo
                                    }
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