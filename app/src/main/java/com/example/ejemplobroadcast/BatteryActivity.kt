package com.example.ejemplobroadcast

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.example.ejemplobroadcast.databinding.ActivityBatteryBinding

class BatteryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBatteryBinding

    //Necesitamos crear un objeto de la clase BradcastReceiver

    private lateinit var myBroadcast: MyBroadcast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatteryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializar una instancia de la clase broadcast receiver

        myBroadcast = MyBroadcast(binding)

        binding.fabSettings.setOnClickListener {
            enabledWriteSettings()
        }
    }

    //Este método va a servir para ir a la pantalla de su celular
    //donde se tiene el listados de apps y los permisos que tienen
    private fun enabledWriteSettings() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        //Registrar su broadcast receiver
        registerReceiver(myBroadcast, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        registerReceiver(myBroadcast, IntentFilter(Intent.ACTION_BATTERY_LOW))
        //Crear un intent, configurarlo, e intentar resolverlo
        val intent = Intent(this,MyBroadcast::class.java)
        sendBroadcast(intent)
    }

    override fun onStop() {
        super.onStop()
        //Quitar el registro de ese bradcastReceiver
        unregisterReceiver(myBroadcast)
    }
}