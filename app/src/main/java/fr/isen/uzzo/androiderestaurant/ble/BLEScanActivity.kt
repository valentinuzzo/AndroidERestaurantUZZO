package fr.isen.uzzo.androiderestaurant.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.uzzo.androiderestaurant.BLEAScanAdapter
import fr.isen.uzzo.androiderestaurant.R
import fr.isen.uzzo.androiderestaurant.databinding.ActivityBlescanBinding

class BLEScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlescanBinding
    private var isScanning = false
    private lateinit var bleAdapter: BluetoothAdapter
    private val ENABLE_BLUETOOTH_REQUEST_CODE = 1
    private val ALL_PERMISSION_REQUEST_CODE = 100
    private var adapter : BLEAScanAdapter? = null

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blescan)

        val actionBar = supportActionBar
        actionBar!!.title = "Bluetooth"

        binding = ActivityBlescanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when {
            bluetoothAdapter?.isEnabled == true->
                startLeScanBLEWithPermission(true)
            bluetoothAdapter != null ->
                askBluetoothPermission()
            else -> {
                displayNoBLEAvaible()
            }
        }

        binding.titleBle.setOnClickListener {
            startLeScanBLEWithPermission(!isScanning)
        }

        binding.bleState.setOnClickListener {
            startLeScanBLEWithPermission(!isScanning)
        }

        adapter = BLEAScanAdapter(arrayListOf()) {
            val intent = Intent(this, BleDeviceActivity::class.java)
            intent.putExtra(DEVICE_KEY, it)
            startActivity(intent)

        }
        binding.bleScanList.layoutManager = LinearLayoutManager(this)
        binding.bleScanList.adapter = adapter
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d("BLEScanActivity", "result : ${result.device.address}, rssi : ${result.rssi}")
            adapter?.apply{
                addElement(result)
                notifyDataSetChanged()
            }

        }
    }

    private fun askBluetoothPermission() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }

    }

    override fun onStop() {
        super.onStop()
        startLeScanBLEWithPermission(false)
    }

    private fun startLeScanBLEWithPermission(enable: Boolean) {
        if (checkAllPermissionGranted()) {
            startLeScanBLE(enable)
        } else {
            ActivityCompat.requestPermissions(
                this,
                getAllPermissions(),
                ALL_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkAllPermissionGranted(): Boolean {
        return getAllPermissions().all { permission ->
            val test = ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            return@all test
        }
    }

    private fun getAllPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }


    @SuppressLint("MissingPermission")
    private fun startLeScanBLE(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if (enable) {
                isScanning = true
                startScan(scanCallback)
            } else {
                isScanning = false
                stopScan(scanCallback)
            }
            handlePlayPauseAction()
        }
    }


    private fun displayNoBLEAvaible() {
        binding.bleState.isVisible
    }

    private fun handlePlayPauseAction() {
        if (isScanning) {
            binding.bleState.setImageResource(R.drawable.ic_pause)
            binding.titleBle.text = getString(R.string.ble_scan_play)
            binding.scanProgression.isIndeterminate = true
        } else {
            binding.bleState.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            binding.titleBle.text = getString(R.string.ble_scan_pause)
            binding.scanProgression.isIndeterminate = false
        }
    }
    companion object {
        const val DEVICE_KEY = "device"

    }
}



