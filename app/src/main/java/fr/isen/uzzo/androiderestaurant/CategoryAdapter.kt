package fr.isen.uzzo.androiderestaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.uzzo.androiderestaurant.model.Item
import java.util.ArrayList

internal class CategoryAdapter(private var arrayListOf: ArrayList<Item>, val clickListener: (Item) -> Unit) :

    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemTextView: TextView = view.findViewById(R.id.CategoryTitle)
        val image: ImageView = view.findViewById(R.id.itemLogo)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_category, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = arrayListOf[position]
        holder.itemTextView.text = item.name_fr

        val url = item.images[0]
        Picasso.get()
            .load(url.ifEmpty{null})
            .placeholder(R.drawable.uber_eats_logo)
            .fit().centerCrop()
            .into(holder.image)

        holder.itemView.setOnClickListener{
            clickListener(item)
        }
    }

    override fun getItemCount(): Int {
        return arrayListOf.size
    }

}





