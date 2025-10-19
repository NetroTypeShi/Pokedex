package com.example.myapplication.Controller;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
                        int id = obj.getInt("id");
                        String name = obj.getString("name");
                        int height = obj.getInt("height");
                        int weight = obj.getInt("weight");
                        String imageUrl = obj.getJSONObject("sprites").getString("front_default");

                        Pokemon pokemon = new Pokemon(id, name, height, weight, imageUrl);
                        callback.onSuccess(pokemon);
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