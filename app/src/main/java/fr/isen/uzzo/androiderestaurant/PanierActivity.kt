package fr.isen.uzzo.androiderestaurant

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import fr.isen.uzzo.androiderestaurant.databinding.ActivityPanierBinding
import fr.isen.uzzo.androiderestaurant.model.Panier
import fr.isen.uzzo.androiderestaurant.model.PanierItems
import java.io.File

class PanierActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPanierBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPanierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Panier"

        val file = File(cacheDir.absolutePath + "/basket.json")

        if (file.exists()) {
            val panierItems : List<PanierItems> = Gson().fromJson(file.readText(), Panier::class.java).data
            display(panierItems)
        }

        val quantity = getString(R.string.basketTotalQuantity) + this.getSharedPreferences(
            getString(R.string.spFileName),
            Context.MODE_PRIVATE
        ).getInt(getString(R.string.spTotalQuantity), 0).toString() + " article(s)"
        binding.panierTotalQuantity.text = quantity

        val price = getString(R.string.totalPrice) + this.getSharedPreferences(getString(R.string.spFileName),
            Context.MODE_PRIVATE).getFloat(getString(R.string.spTotalPrice), 0.0f).toString() + " € "
        binding.panierTotalPrice.text = price

        binding.panierButtonDelete.setOnClickListener {
            deleteBasketData()
            finish()
        }

        binding.panierButtonBuy.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/photo/?fbid=467156688451972&set=pcb.467162305118077")
                )
            )
        }
    }

    private fun display(itemsList: List<PanierItems>) {
        binding.panierList.layoutManager = LinearLayoutManager(this)
        binding.panierList.adapter = PanierAdapter(itemsList as ArrayList<PanierItems>) {
            deleteItemBasket(it)
        }
    }
    private fun deleteItemBasket(item: PanierItems) {
        val file = File(cacheDir.absolutePath + "/basket.json")
        var itemBasket: List<PanierItems> = ArrayList()

        if (file.exists()) {
            itemBasket = Gson().fromJson(file.readText(), Panier::class.java).data
            itemBasket = itemBasket - item
            updateSharedPreferences(item.quantity, item.item.prices[0].price.toInt())
        }

        file.writeText(Gson().toJson(Panier(itemBasket)))

        finish()
        this.recreate()
    }

    private fun deleteBasketData() {
        File(cacheDir.absolutePath + "/basket.json").delete()
        this.getSharedPreferences(getString(R.string.spFileName), Context.MODE_PRIVATE).edit().remove(getString(R.string.spTotalPrice)).apply()
        this.getSharedPreferences(getString(R.string.spFileName), Context.MODE_PRIVATE).edit().remove(getString(R.string.spTotalQuantity)).apply()
        Toast.makeText(this, getString(R.string.basketDeleteAllTxt), Toast.LENGTH_SHORT).show()
    }

    private fun updateSharedPreferences(quantity: Int, price: Int) {
        val sharedPreferences = this.getSharedPreferences(getString(R.string.spFileName), Context.MODE_PRIVATE)

        val oldQuantity = sharedPreferences.getInt(getString(R.string.spTotalQuantity), 0)
        val newQuantity = oldQuantity + quantity
        sharedPreferences.edit().putInt(getString(R.string.spTotalQuantity), newQuantity).apply()

        val oldPrice = sharedPreferences.getFloat(getString(R.string.spTotalPrice), 0.0f)
        val newPrice = oldPrice - price
        sharedPreferences.edit().putFloat(getString(R.string.spTotalPrice), newPrice).apply()
    }

}



