package fr.isen.uzzo.androiderestaurant.model

import java.io.Serializable


data class Category(val name_fr: String, val items: ArrayList<Item>): Serializable
