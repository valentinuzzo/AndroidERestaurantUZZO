package fr.isen.uzzo.androiderestaurant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.uzzo.androiderestaurant.model.PanierItems

internal class PanierAdapter(
    val baskets: List<PanierItems>,
    val onBasketClick: (PanierItems) -> Unit) : RecyclerView.Adapter<PanierAdapter.BasketViewHolder>() {

    internal inner class BasketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.panierCellTitle)
        val price: TextView = view.findViewById(R.id.panierCellPrice)
        val quantitypanier: TextView = view.findViewById(R.id.panierCellQuantity)
        val delete: ImageView = view.findViewById(R.id.panierCellDelete)
        val imagePlat: ImageView = view.findViewById(R.id.panierImagePlat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_panier_adapter, parent, false)
        return BasketViewHolder(itemView)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val basket = baskets[position]

        holder.name.text = basket.item.name_fr

        val url = basket.item.images[0]
        Picasso.get()
            .load(url.ifEmpty{null})
            .placeholder(R.drawable.uber_eats_logo)
            .fit().centerCrop()
            .into(holder.imagePlat)

        val price = "Total : ${basket.item.prices[0].price.toFloat() * basket.quantity} €"
        holder.price.text = price

        val quantity = "Quantité : ${basket.quantity}"
        holder.quantitypanier.text = quantity

        holder.delete.setOnClickListener {
            onBasketClick(basket)
        }
    }

    override fun getItemCount(): Int {
        return baskets.size
    }
}
