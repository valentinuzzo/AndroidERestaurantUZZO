package fr.isen.uzzo.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
}




