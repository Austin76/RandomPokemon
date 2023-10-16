package com.example.randompokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.bumptech.glide.Glide
import android.util.Log
import okhttp3.Headers
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var pokemonImageURL =""

    // UI components
    private lateinit var button: Button
    private lateinit var imageView: ImageView
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var searchButton: Button

    // Initialize the activity view and set upUI listeners
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<Button>(R.id.pokemonButton)
        imageView = findViewById<ImageView>(R.id.pokemonImage)
        textView1 = findViewById<TextView>(R.id.pokemonName)
        textView2 = findViewById<TextView>(R.id.pokedexNumber)
        searchButton = findViewById<Button>(R.id.searchButton)

        setupListeners()
    }

    // Sets up button click listeners for getting random Pokemon and searching for specific Pokemon
    private fun setupListeners() {
        button.setOnClickListener {
            getRandomPokemonImage(imageView, textView1, textView2)
        }

        searchButton.setOnClickListener {
            searchPokemon(imageView, textView1, textView2)
        }
    }

    // Fetches Pokemon details directly from the API and updates the UI
    private fun fetchPokemon(apiURL: String, imageView: ImageView, nameTextView: TextView, numberTextView: TextView) {
        val client = AsyncHttpClient()

        client.get(apiURL, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val pokemonName = json.jsonObject.getString("name")
                val pokemonNumber = json.jsonObject.getInt("id")

                val sprites = json.jsonObject.getJSONObject("sprites")
                val imageUrl = sprites.getString("front_default")

                Glide.with(this@MainActivity)
                    .load(imageUrl)
                    .fitCenter()
                    .into(imageView)

                nameTextView.text = "Name: $pokemonName"
                numberTextView.text = "Pokedex Number: $pokemonNumber"
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.d("Pokemon Error", errorResponse)
            }
        })
    }

    // Fetches a random Pokemon's details and updates the UI
    private fun getRandomPokemonImage(imageView: ImageView, nameTextView: TextView, numberTextView: TextView) {
        val randomPokemonId = Random.nextInt(1, 1018) // Scarlet and Violet DLC!
        val apiURL = "https://pokeapi.co/api/v2/pokemon/$randomPokemonId/"
        fetchPokemon(apiURL, imageView, nameTextView, numberTextView)
    }

    // Searches for a specific Pokemon based on an input name or ID and updates the UI
    private fun searchPokemon(imageView: ImageView, nameTextView: TextView, numberTextView: TextView) {
        val editText = findViewById<EditText>(R.id.searchEditText)
        val pokemonNameOrId = editText.text.toString().trim()

        if (pokemonNameOrId.isEmpty()) {
            return
        }

        val apiURL = "https://pokeapi.co/api/v2/pokemon/$pokemonNameOrId/"
        fetchPokemon(apiURL, imageView, nameTextView, numberTextView)
    }

}