package fr.isen.uzzo.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.helper.widget.Carousel
import com.google.android.material.snackbar.Snackbar
import fr.isen.uzzo.androiderestaurant.databinding.ActivityDetailBinding
import fr.isen.uzzo.androiderestaurant.model.Item

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getSerializableExtra(CategoryActivity.ITEM_KEY) as Item
        binding.titrePlat.text = item.name_fr

        binding.detail.text = item.ingredients.joinToString ( ", " ){it.name_fr}
        binding.buttonPrice.text = item.prices.joinToString(", "){"Total: "+ it.price.toString()}

        val carouselAdapter = CarouselAdapter(this, item.images)

        binding.detailSlider.adapter = carouselAdapter

        button = findViewById(R.id.buttonPrice)

        button.setOnClickListener {
            val snack = Snackbar.make(it,"ajoutez au panier",Snackbar.LENGTH_LONG)
            snack.show()
        }

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
                Toast.makeText(this@DetailActivity, "Panier", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.buttonBle -> {
                Toast.makeText(this@DetailActivity, "Bluetooth", Toast.LENGTH_SHORT).show()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}




