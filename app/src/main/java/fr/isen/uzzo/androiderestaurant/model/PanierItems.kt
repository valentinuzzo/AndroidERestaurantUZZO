package fr.isen.uzzo.androiderestaurant.model

import java.io.Serializable

data class PanierItems(
    val item: Item,
    var quantity: Int
) : Serializable
