package fr.isen.uzzo.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import fr.isen.uzzo.androiderestaurant.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding   //declaration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)   //initialisation
        setContentView(binding.root)    //utilisation

        binding.BoutonEntrees.setOnClickListener{
            Toast.makeText(
                this@HomeActivity,
                "Redirection vers la page d'accueil",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.BoutonEntrees.setOnClickListener{
           goToCategory(getString(R.string.home_starters))
        }

        binding.BoutonPlats.setOnClickListener{
            goToCategory(getString(R.string.home_dish))
        }

        binding.BoutonDesserts.setOnClickListener{
            goToCategory(getString(R.string.home_desert))
        }

    }

    private fun goToCategory(category: String) {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }
}

