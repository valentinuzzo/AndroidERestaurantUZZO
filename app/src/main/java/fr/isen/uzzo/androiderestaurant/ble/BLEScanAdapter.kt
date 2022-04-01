package fr.isen.uzzo.androiderestaurant

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


class BLEAScanAdapter(val data: ArrayList<ScanResult>, val result: (BluetoothDevice) -> Unit) :
    RecyclerView.Adapter<BLEAScanAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var adresse: TextView = view.findViewById(R.id.bleAddress)
        var name: TextView = view.findViewById(R.id.bleName)
        var rssi: TextView = view.findViewById(R.id.bleRssi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val bleView =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_blescan_adapter, parent, false)
        return MyViewHolder(bleView)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.i("XXX", "onBindViewHolder")
        val appareil = data[position]
        holder.adresse.text = appareil.device.address
        holder.name.text = appareil.device.name
        holder.rssi.text = appareil.rssi.toString()

        holder.itemView.setOnClickListener {
            result(appareil.device)
        }
    }

    fun addElement(result: ScanResult) {
        val indexOfResult = data.indexOfFirst {
            it.device.address == result.device.address
        }
        if (indexOfResult != -1) {
            data[indexOfResult] = result
        } else {
            data.add(result)
            //notifyItemInserted(data.size -1)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}
