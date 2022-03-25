package fr.isen.uzzo.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.uzzo.androiderestaurant.databinding.ActivityCategoryBinding
import fr.isen.uzzo.androiderestaurant.model.DataResult
import org.json.JSONObject


class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val categoryName = intent.getStringExtra("category")
        binding.category.text = categoryName


        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.adapter = CategoryAdapter(arrayListOf()) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(ITEM_KEY, it)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)



        loadDataFromServerByCategory(intent.getStringExtra("category") ?: "")
    }



    companion object{
        val ITEM_KEY = "item"
    }

    private fun loadDataFromServerByCategory(category : String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://test.api.catering.bluecodegames.com/menu"

        val jsonObject = JSONObject()
        jsonObject.put("id_shop", 1)

        val stringReq =JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
        { response ->

                    // response
                    val strResp = response.toString()
                    val dataResult = Gson().fromJson(strResp, DataResult::class.java)

                    val items = dataResult.data.firstOrNull { it.name_fr == category}?.items ?: arrayListOf()
                    binding.recyclerView.adapter = CategoryAdapter(items){
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra(ITEM_KEY, it)
                        startActivity(intent)
                    }

                    Log.d("API", strResp)
                },{

                    Log.d("API", "message ${it.message}")
                })

        queue.add(stringReq)
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
                Toast.makeText(this@CategoryActivity, "Panier", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.buttonBle -> {
                Toast.makeText(this@CategoryActivity, "Bluetooth", Toast.LENGTH_SHORT).show()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

}






