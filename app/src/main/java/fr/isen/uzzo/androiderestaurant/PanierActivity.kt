package fr.isen.uzzo.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import fr.isen.uzzo.androiderestaurant.databinding.ActivityPanierBinding
import fr.isen.uzzo.androiderestaurant.model.Panier
import fr.isen.uzzo.androiderestaurant.model.PanierItems
import java.io.File

class PanierActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panier)

    }
}