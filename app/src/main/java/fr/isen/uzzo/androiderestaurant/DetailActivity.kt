package fr.isen.uzzo.androiderestaurant

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import fr.isen.uzzo.androiderestaurant.CategoryActivity.Companion.ITEM_KEY
import fr.isen.uzzo.androiderestaurant.ble.BLEScanActivity
import fr.isen.uzzo.androiderestaurant.databinding.ActivityDetailBinding
import fr.isen.uzzo.androiderestaurant.model.Item
import fr.isen.uzzo.androiderestaurant.model.Panier
import fr.isen.uzzo.androiderestaurant.model.PanierItems
import java.io.File

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var button: Button
    lateinit var buttonReset: Button
    lateinit var quantity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val actionBar = supportActionBar
        actionBar!!.title = "Details"

        button = findViewById(R.id.buttonPrice)
        button.setOnClickListener{
            val snackbar = Snackbar.make(it, "Produit ajouté au panier", Snackbar.LENGTH_LONG)
            val intent = Intent(this@DetailActivity, PanierActivity::class.java)
            startActivity(intent)
            snackbar.show()
        }

        val item = intent.getSerializableExtra(ITEM_KEY) as Item

        binding.titrePlat.text = item.name_fr
        binding.detail.text = item.ingredients.joinToString(", "){it.name_fr}
        binding.buttonPrice.text = item.prices.joinToString(", "){"Ajouter au panier: "+ it.price.toString()}

        val carousselAdapter = CarouselAdapter(this, item.images)

        binding.detailSlider.adapter = carousselAdapter

        addToPanier(item)
    }

    @SuppressLint("setTextI18n")
    private fun display(somme: Int, price: Float) {
        binding.quantity.text = somme.toString()
        binding.buttonPrice.text = "Total : " + price.toString() + "€"
    }



    private fun updateSharedPreferences(quantity: Int, price: Float) {
        val sharedPreferences = this.getSharedPreferences(getString(R.string.spFileName), Context.MODE_PRIVATE)

        val oldQuantity = sharedPreferences.getInt(getString(R.string.spTotalQuantity), 0)
        val newQuantity = oldQuantity + quantity
        sharedPreferences.edit().putInt(getString(R.string.spTotalQuantity), newQuantity).apply()

        val oldPrice = sharedPreferences.getFloat(getString(R.string.spTotalPrice), 0.0f)
        val newPrice = oldPrice + price
        sharedPreferences.edit().putFloat(getString(R.string.spTotalPrice), newPrice).apply()
    }


    private fun addToPanier(item: Item) {
        var nbInShop = 1
        binding.buttonAdd.setOnClickListener()
        {
            nbInShop++
            Log.i("nbInShop", nbInShop.toString())
            display(nbInShop, nbInShop * item.prices[0].price.toFloat())
        }

        binding.buttonRemove.setOnClickListener()
        {

            if (nbInShop > 1) {
                nbInShop--
                Log.i("nbInShop", nbInShop.toString())
                display(nbInShop, nbInShop * item.prices[0].price.toFloat())
            }
        }

        var tv = findViewById<TextView>(R.id.quantity)
        quantity = findViewById(R.id.quantity)
        quantity.text = nbInShop.toString()
        totalComplete(item, nbInShop)
        buttonReset = findViewById(R.id.buttonReset)
        buttonReset.setOnClickListener {

            Toast.makeText(this@DetailActivity, "Suppression des éléments", Toast.LENGTH_SHORT)
                .show()
            nbInShop = 1
            if(nbInShop>1)
            {
                nbInShop=1
            }
            tv.setText("$nbInShop")
            quantity.text = nbInShop.toString()
            totalComplete(item, nbInShop)
        }


        binding.titrePlat.text = item.name_fr

        val txt = getString(R.string.totalPrice) + item.prices[0].price + " €"
        binding.buttonPrice.text = txt

        binding.buttonPrice.setOnClickListener {
                Snackbar.make(
                    it, "Article(s) ajouté(s) au panier : " + nbInShop + " " + binding.titrePlat.text,
                    Snackbar.LENGTH_LONG
                ).setAction("Afficher le panier") {
                    startActivity(Intent(this, PanierActivity::class.java))
                }
                    .show()
                updateFile(PanierItems(item, nbInShop))
                updateSharedPreferences(nbInShop, (item.prices[0].price.toFloat() * nbInShop))
        }
    }

    private fun updateFile(itemBasket: PanierItems) {
        val file = File(cacheDir.absolutePath + "/basket.json")
        var itemsBasket: List<PanierItems> = ArrayList()

        if (file.exists()) {
            itemsBasket = Gson().fromJson(file.readText(), Panier::class.java).data
        }

        var dupli = false
        for (i in itemsBasket.indices) {
            if (itemsBasket[i].item == itemBasket.item) {
                itemsBasket[i].quantity += itemBasket.quantity
                dupli = true
            }
        }

        if (!dupli) {
            itemsBasket = itemsBasket + itemBasket
        }

        file.writeText(Gson().toJson(Panier(itemsBasket)))
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

    private fun totalComplete(item: Item, selected: Int) {
        val totalPrice: String = item.prices[0].price
        val total1: Float = totalPrice.toFloat() * selected
        val totalString: String = "Total : " + total1.toString() + "€"
        binding.buttonPrice.text = totalString
    }
}



