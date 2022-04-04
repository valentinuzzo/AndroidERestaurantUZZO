package fr.isen.uzzo.androiderestaurant

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import fr.isen.uzzo.androiderestaurant.ble.BLEScanActivity
import fr.isen.uzzo.androiderestaurant.databinding.ActivityDetailBinding
import fr.isen.uzzo.androiderestaurant.model.Dish
import fr.isen.uzzo.androiderestaurant.model.Item
import fr.isen.uzzo.androiderestaurant.model.Panier
import fr.isen.uzzo.androiderestaurant.model.PanierItems
import java.io.File

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var button: Button
    lateinit var buttonAdd: ImageView
    lateinit var buttonRemove: ImageView
    lateinit var quantity: TextView
    lateinit var buttonReset: Button
    var num: Float = 1F


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
            val snack = Snackbar.make(it,"ajout au panier",Snackbar.LENGTH_LONG)
            snack.show()
        }
        var tv = findViewById<TextView>(R.id.quantity)

        quantity = findViewById(R.id.quantity)
        quantity.text = num.toString()
        totalComplete(item, num)

// set on-click listener for ImageView

        buttonAdd = findViewById(R.id.buttonAdd)
        buttonAdd.setOnClickListener {

            num++
            tv.setText("$num")
            quantity.text = num.toString()
            totalComplete(item, num)
        }
        buttonRemove = findViewById(R.id.buttonRemove)
        buttonRemove.setOnClickListener {
            if(num<2F)
            {
                num = 2F
            }
            num--
            tv.setText("$num")
            quantity.text = num.toString()
            totalComplete(item, num)
        }
        buttonReset = findViewById(R.id.buttonReset)
        buttonReset.setOnClickListener {

            Toast.makeText(this@DetailActivity, "Suppression des éléments", Toast.LENGTH_SHORT)
                .show()
            num = 1F
            tv.setText("$num")
            quantity.text = num.toString()
            totalComplete(item, num)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun goToBle() {
        val intent = Intent(this, BLEScanActivity::class.java)
        startActivity(intent)
    }

    private fun goToPanier() {
        val intent = Intent(this, PanierActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.buttonPanier -> {
                Toast.makeText(this@DetailActivity, "Panier", Toast.LENGTH_SHORT).show()
                goToPanier()

                return true
            }
            R.id.buttonBle -> {
                Toast.makeText(this@DetailActivity, "Bluetooth", Toast.LENGTH_SHORT).show()
                goToBle()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun totalComplete(item: Item, selected: Float) {
        val totalPrice: String = item.prices[0].price
        val total1: Float = totalPrice.toFloat() * selected
        val totalString: String = "Total : " + total1.toString() + "€"
        binding.buttonPrice.text = totalString
    }



//CHANGE PRICE
/*
    private fun changePrice(dish: Dish, nb: Int) {
        var newPrice = dish.prices[0].price.toFloatOrNull()
        newPrice = newPrice?.times(nb)
        binding.buttonAdd.ImageView = "$newPrice €"
    }*/
    private fun updateFile(dishBasket : PanierItems) {
        val file = File(cacheDir.absolutePath + "/basket.json")
        var dishesBasket: List<PanierItems> = ArrayList()

        if (file.exists()) {
            dishesBasket = Gson().fromJson(file.readText(), Panier::class.java).data
        }

        var dupli = false
        for (i in dishesBasket.indices) {
            if (dishesBasket[i].dish == dishBasket.dish) {
                dishesBasket[i].quantity += dishBasket.quantity
                dupli = true
            }
        }

        if (!dupli) {
            dishesBasket = dishesBasket + dishBasket
        }

        file.writeText(Gson().toJson(Panier(dishesBasket)))
    }
/*
    private fun updateSharedPreferences(quantity: Int, price: Float) {
        val sharedPreferences = this.getSharedPreferences(getString(R.string.spFileName), Context.MODE_PRIVATE)

        val oldQuantity = sharedPreferences.getInt(getString(R.string.spTotalQuantity), 0)
        val newQuantity = oldQuantity + quantity
        sharedPreferences.edit().putInt(getString(R.string.spTotalQuantity), newQuantity).apply()

        val oldPrice = sharedPreferences.getFloat(getString(R.string.spTotalPrice), 0.0f)
        val newPrice = oldPrice + price
        sharedPreferences.edit().putFloat(getString(R.string.spTotalPrice), newPrice).apply()
    }*/

    private fun changeActivity() {
        val intent = Intent(this@DetailActivity, PanierActivity::class.java)
        startActivity(intent)
    }
}









