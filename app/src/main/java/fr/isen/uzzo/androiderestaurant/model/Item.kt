package fr.isen.uzzo.androiderestaurant.model

import java.io.Serializable

data class Item(
    val name_fr: String,
    val ingredients: ArrayList<Ingredient>,
    val images: ArrayList<String>,
    val prices: ArrayList<Price>
    ): Serializable

