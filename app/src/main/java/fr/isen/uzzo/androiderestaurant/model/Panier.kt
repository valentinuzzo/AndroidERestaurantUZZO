package fr.isen.uzzo.androiderestaurant.model

import java.io.Serializable

data class Panier (
    val data : List<PanierItems>
) : Serializable
