package fr.isen.uzzo.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import fr.isen.uzzo.androiderestaurant.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"

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

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Mon activité est détruite")

    }

    private fun goToCategory(category: String) {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.buttonPanier -> {
                Toast.makeText(this@HomeActivity, "Panier", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.buttonBle -> {
                Toast.makeText(this@HomeActivity, "Bluetooth", Toast.LENGTH_SHORT).show()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}


