package fr.isen.uzzo.androiderestaurant.model

import java.io.Serializable

data class PanierItems (
    val dish : Dish,
    var quantity : Int
) : Serializable
