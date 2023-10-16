package com.example.randompokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.bumptech.glide.Glide
import android.util.Log
import okhttp3.Headers
import android.widget.Button
import android.widget.ImageView

import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var pokemonImageURL =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPokemonImageURL()
        Log.d("pokemonImageURL", "pokemon image URL set")

        val button = findViewById<Button>(R.id.pokemonButton)
        val imageView = findViewById<ImageView>(R.id.pokemonImage)
        val textView1 = findViewById<TextView>(R.id.pokemonInfo1)
        val textView2 = findViewById<TextView>(R.id.pokemonInfo2)

        getNextImage(button, imageView)
    }

    private fun getPokemonImageURL() {
        val client = AsyncHttpClient()

        client["", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Pokemon", "response successful$json")
                pokemonImageURL = json.jsonObject.getString("message")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Pokemon Error", errorResponse)
            }
        }]
    }

    private fun getNextImage(button: Button, imageView: ImageView) {
        button.setOnClickListener {
            getPokemonImageURL()

            Glide.with(this)
                .load(pokemonImageURL)
                .fitCenter()
                .into(imageView)
        }
    }
}